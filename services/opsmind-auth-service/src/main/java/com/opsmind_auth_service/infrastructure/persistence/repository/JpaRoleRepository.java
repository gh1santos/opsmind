package com.opsmind_auth_service.infrastructure.persistence.repository;

import com.opsmind_auth_service.domain.enums.UserRole;
import com.opsmind_auth_service.infrastructure.persistence.entity.RoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRoleRepository extends JpaRepository<RoleJpaEntity, Long> {

    Optional<RoleJpaEntity> findByName(UserRole name);
}