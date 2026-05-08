package com.opsmind_auth_service.domain.repository;

import com.opsmind_auth_service.domain.entity.Role;
import com.opsmind_auth_service.domain.enums.UserRole;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByName(UserRole role);
}

