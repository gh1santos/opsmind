package com.opsmind_auth_service.infrastructure.persistence.adapter;

import com.opsmind_auth_service.domain.entity.RefreshToken;
import com.opsmind_auth_service.domain.repository.RefreshTokenRepository;
import com.opsmind_auth_service.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import com.opsmind_auth_service.infrastructure.persistence.repository.JpaRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter
        implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository repository;

    @Override
    public RefreshToken save(RefreshToken token) {

        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setToken(token.getToken());
        entity.setUserId(token.getUserId());
        entity.setUserEmail(token.getUserEmail());
        entity.setUserRole(token.getUserRole());
        entity.setTenantId(token.getTenantId());
        entity.setReplacedByToken(token.getReplacedByToken());
        entity.setRevoked(token.isRevoked());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setCreatedAt(token.getCreatedAt());

        RefreshTokenJpaEntity saved = repository.save(entity);

        return toModel(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token).map(this::toModel);
    }

    @Override
    public List<RefreshToken> findAllByUserId(Long userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void revokeByToken(String token) {
        repository.revokeByToken(token);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(Long userId) {
        repository.revokeAllByUserId(userId);
    }

    private RefreshToken toModel(RefreshTokenJpaEntity entity) {
        return RefreshToken.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .userId(entity.getUserId())
                .userEmail(entity.getUserEmail())
                .userRole(entity.getUserRole())
                .tenantId(entity.getTenantId())
                .replacedByToken(entity.getReplacedByToken())
                .revoked(entity.isRevoked())
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
