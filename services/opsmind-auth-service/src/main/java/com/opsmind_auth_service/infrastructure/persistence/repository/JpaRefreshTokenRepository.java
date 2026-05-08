package com.opsmind_auth_service.infrastructure.persistence.repository;

import com.opsmind_auth_service.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRefreshTokenRepository
        extends JpaRepository<RefreshTokenJpaEntity, Long> {

    Optional<RefreshTokenJpaEntity> findByToken(String token);
}
