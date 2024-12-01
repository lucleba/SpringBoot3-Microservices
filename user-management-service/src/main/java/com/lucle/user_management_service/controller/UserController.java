package com.lucle.user_management_service.controller;

import com.lucle.user_management_service.dto.response.ApiResponse;
import com.lucle.user_management_service.dto.request.UserCreationRequest;
import com.lucle.user_management_service.dto.request.UserUpdateRequest;
import com.lucle.user_management_service.dto.response.UserResponse;
import com.lucle.user_management_service.entity.User;
import com.lucle.user_management_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    final UserService userService; //su dung final de khai bao cho RequiredArgsConstructor

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        log.info("Controller: create User");
        return ApiResponse.<UserResponse>builder()
                .code(1000)
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(){
        // lay thong tin nguoi dung hien tai trong 1 request
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("username: {}", authentication.getName());
        authentication.getAuthorities().forEach(
                grantedAuthority -> log.info("Roles: {}", grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(id))
                .build();
    }

    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request) {

        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder()
                .result("User deleted successfully")
                .build();
    }

}
