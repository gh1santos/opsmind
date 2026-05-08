package com.opsmind_auth_service.infrastructure.persistence.adapter;

import com.opsmind_auth_service.domain.entity.Role;
import com.opsmind_auth_service.domain.enums.UserRole;
import com.opsmind_auth_service.domain.repository.RoleRepository;
import com.opsmind_auth_service.infrastructure.persistence.entity.RoleJpaEntity;
import com.opsmind_auth_service.infrastructure.persistence.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final JpaRoleRepository jpaRoleRepository;

    @Override
    public Optional<Role> findByName(UserRole role) {

        return jpaRoleRepository
                .findByName(role)
                .map(this::toDomain);
    }

    private Role toDomain(RoleJpaEntity entity) {

        return Role.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
