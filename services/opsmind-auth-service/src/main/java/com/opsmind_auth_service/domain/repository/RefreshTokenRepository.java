package com.opsmind_auth_service.domain.repository;

import com.opsmind_auth_service.domain.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);
}
