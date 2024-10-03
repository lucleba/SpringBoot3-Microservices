package com.lucle.user_management_service.repository;

import com.lucle.user_management_service.entity.Permission;
import com.lucle.user_management_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {


}
