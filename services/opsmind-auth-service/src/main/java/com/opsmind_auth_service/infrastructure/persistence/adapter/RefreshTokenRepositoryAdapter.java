package com.opsmind_auth_service.infrastructure.persistence.adapter;

import com.opsmind_auth_service.domain.entity.RefreshToken;
import com.opsmind_auth_service.domain.repository.RefreshTokenRepository;
import com.opsmind_auth_service.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import com.opsmind_auth_service.infrastructure.persistence.repository.JpaRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter
        implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository repository;

    @Override
    public RefreshToken save(RefreshToken token) {

        RefreshTokenJpaEntity entity =
                new RefreshTokenJpaEntity();

        entity.setToken(token.getToken());
        entity.setUserId(token.getUserId());
        entity.setRevoked(token.isRevoked());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setCreatedAt(token.getCreatedAt());

        RefreshTokenJpaEntity saved =
                repository.save(entity);

        return RefreshToken.builder()
                .id(saved.getId())
                .token(saved.getToken())
                .userId(saved.getUserId())
                .revoked(saved.isRevoked())
                .expiresAt(saved.getExpiresAt())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {

        return repository.findByToken(token)
                .map(entity ->
                        RefreshToken.builder()
                                .id(entity.getId())
                                .token(entity.getToken())
                                .userId(entity.getUserId())
                                .revoked(entity.isRevoked())
                                .expiresAt(entity.getExpiresAt())
                                .createdAt(entity.getCreatedAt())
                                .build()
                );
    }
}
