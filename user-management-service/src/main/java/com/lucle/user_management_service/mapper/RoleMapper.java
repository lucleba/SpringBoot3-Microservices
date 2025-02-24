package com.lucle.user_management_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lucle.user_management_service.dto.request.RoleRequest;
import com.lucle.user_management_service.dto.response.RoleResponse;
import com.lucle.user_management_service.entity.Role;

@Mapper(componentModel = "spring") // khai bao mapper su dung trong spring, Autowired de su dung
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
