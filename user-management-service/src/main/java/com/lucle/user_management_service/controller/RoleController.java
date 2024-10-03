package com.lucle.user_management_service.controller;

import com.lucle.user_management_service.dto.request.PermissionRequest;
import com.lucle.user_management_service.dto.request.RoleRequest;
import com.lucle.user_management_service.dto.response.ApiResponse;
import com.lucle.user_management_service.dto.response.PermissionResponse;
import com.lucle.user_management_service.dto.response.RoleResponse;
import com.lucle.user_management_service.service.PermissionService;
import com.lucle.user_management_service.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleService.create(request));

        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        ApiResponse<List<RoleResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleService.getAll());

        return apiResponse;
    }

    @DeleteMapping("/{role}")
    public ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);

        return ApiResponse.<Void>builder().build();
    }
}
