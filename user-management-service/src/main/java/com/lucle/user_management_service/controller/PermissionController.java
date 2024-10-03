package com.lucle.user_management_service.controller;

import com.lucle.user_management_service.dto.request.PermissionRequest;
import com.lucle.user_management_service.dto.request.UserCreationRequest;
import com.lucle.user_management_service.dto.response.ApiResponse;
import com.lucle.user_management_service.dto.response.PermissionResponse;
import com.lucle.user_management_service.entity.User;
import com.lucle.user_management_service.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody @Valid PermissionRequest request) {
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(permissionService.create(request));

        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(permissionService.getAll());

        return apiResponse;
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Void> delete(@PathVariable String permission) {
        permissionService.delete(permission);

        return ApiResponse.<Void>builder().build();
    }
}
