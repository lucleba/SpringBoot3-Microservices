package com.lucle.user_management_service.mapper;

import com.lucle.user_management_service.dto.request.UserCreationRequest;
import com.lucle.user_management_service.dto.request.UserUpdateRequest;
import com.lucle.user_management_service.dto.response.UserResponse;
import com.lucle.user_management_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // khai bao mapper su dung trong spring, Autowired de su dung
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);

    @Mapping(target = "password", ignore = true)
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
