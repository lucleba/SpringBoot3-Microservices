package com.lucle.user_management_service.service;

import com.lucle.user_management_service.dto.request.UserCreationRequest;
import com.lucle.user_management_service.dto.request.UserUpdateRequest;
import com.lucle.user_management_service.dto.response.UserResponse;
import com.lucle.user_management_service.entity.User;
import com.lucle.user_management_service.exception.AppException;
import com.lucle.user_management_service.exception.ErrorCode;
import com.lucle.user_management_service.mapper.UserMapper;
import com.lucle.user_management_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    final UserRepository userRepository; // su dung final de khai bao cho RequiredArgsConstructor
    final UserMapper userMapper;

    public User createUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        User user = userMapper.toUser(request);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String userId,UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

}
