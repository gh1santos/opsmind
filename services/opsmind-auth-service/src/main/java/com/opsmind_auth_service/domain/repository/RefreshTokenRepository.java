package com.opsmind_auth_service.domain.repository;

import com.opsmind_auth_service.domain.entity.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUserId(Long userId);

    void revokeByToken(String token);

    void revokeAllByUserId(Long userId);
}
