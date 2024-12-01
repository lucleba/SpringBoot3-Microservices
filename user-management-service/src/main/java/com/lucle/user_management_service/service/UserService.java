package com.lucle.user_management_service.service;

import com.lucle.user_management_service.dto.request.UserCreationRequest;
import com.lucle.user_management_service.dto.request.UserUpdateRequest;
import com.lucle.user_management_service.dto.response.UserResponse;
import com.lucle.user_management_service.entity.User;
import com.lucle.user_management_service.enums.Role;
import com.lucle.user_management_service.exception.AppException;
import com.lucle.user_management_service.exception.ErrorCode;
import com.lucle.user_management_service.mapper.UserMapper;
import com.lucle.user_management_service.repository.RoleRepository;
import com.lucle.user_management_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository; // su dung final de khai bao cho RequiredArgsConstructor
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {
        log.info("Service: Create User");

        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        // user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

//    @PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PreAuthorize("hasAuthority('APPROVE_POST')")
    public List<UserResponse> getAllUser() {
        log.info("In method get all user.");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

//    https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html
//    @PostAuthorize("hasRole('ADMIN')")
    @PostAuthorize("returnObject.username == authentication.name") // lay thong tin cua chính mình
    public UserResponse getUser(String id){
        log.info("in method get user by id");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }


//    https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-securitycontextholder
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return  userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId,UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }



}
