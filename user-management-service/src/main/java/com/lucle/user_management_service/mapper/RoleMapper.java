package com.lucle.user_management_service.mapper;

import com.lucle.user_management_service.dto.request.PermissionRequest;
import com.lucle.user_management_service.dto.request.RoleRequest;
import com.lucle.user_management_service.dto.response.PermissionResponse;
import com.lucle.user_management_service.dto.response.RoleResponse;
import com.lucle.user_management_service.entity.Permission;
import com.lucle.user_management_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // khai bao mapper su dung trong spring, Autowired de su dung
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}
