package com.lucle.user_management_service.service;

import com.lucle.user_management_service.dto.request.AuthenticationRequest;
import com.lucle.user_management_service.dto.request.IntrospectRequest;
import com.lucle.user_management_service.dto.request.LogoutRequest;
import com.lucle.user_management_service.dto.request.RefreshRequest;
import com.lucle.user_management_service.dto.response.AuthenticationResponse;
import com.lucle.user_management_service.dto.response.IntrospectResponse;
import com.lucle.user_management_service.entity.InvalidatedToken;
import com.lucle.user_management_service.entity.User;
import com.lucle.user_management_service.exception.AppException;
import com.lucle.user_management_service.exception.ErrorCode;
import com.lucle.user_management_service.repository.InvalidatedTokenRepository;
import com.lucle.user_management_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal // danh dau de khong inject vao contractor
    @Value("${jwt.signerKey}")
    protected String signerKey;

    @NonFinal // danh dau de khong inject vao contractor
    @Value("${jwt.valid-duration}")
    protected int validDuration;

    @NonFinal // danh dau de khong inject vao contractor
    @Value("${jwt.refreshable-duration}")
    protected int refreshableDuration;


    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        // passwordEncoder.matches de kiem tra xem password nhap vao co khop voi password trong database khong
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        String jwtId = UUID.randomUUID().toString();
        var token = generateToken(user, jwtId, false);
        var refreshToken = generateToken(user, jwtId, true);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken());

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var type = signedJWT.getJWTClaimsSet().getClaim("typ").toString();
        if (!"Refresh".equals(type))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        String jwtId = UUID.randomUUID().toString();
        var token = generateToken(user, jwtId, false);
        var refreshToken = generateToken(user, jwtId, true);

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user, String jwtId, boolean isrefreshable) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("lucle.com")
                .issueTime(new Date())
                .expirationTime(
                        isrefreshable ? new Date(
                                Instant.now().plus(refreshableDuration, ChronoUnit.SECONDS).toEpochMilli()
                        ):
                        new Date(
                                Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()
                        )
                )
                .jwtID(jwtId)
                .claim("scope", buildScope(user))
                .claim("typ", isrefreshable ? "Refresh" : "Bearer")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can not generate token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role ->{
                stringJoiner.add("ROLE_"+role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return  stringJoiner.toString();
    }
}