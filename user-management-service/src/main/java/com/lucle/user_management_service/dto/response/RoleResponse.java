package com.lucle.user_management_service.dto.response;

import java.util.Set;

import com.lucle.user_management_service.entity.Permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String name;
    String description;

    Set<Permission> permissions;
}
