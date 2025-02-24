package com.lucle.user_management_service.mapper;

import org.mapstruct.Mapper;

import com.lucle.user_management_service.dto.request.PermissionRequest;
import com.lucle.user_management_service.dto.response.PermissionResponse;
import com.lucle.user_management_service.entity.Permission;

@Mapper(componentModel = "spring") // khai bao mapper su dung trong spring, Autowired de su dung
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
