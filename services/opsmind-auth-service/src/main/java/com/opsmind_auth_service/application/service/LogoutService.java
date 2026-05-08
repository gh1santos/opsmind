package com.opsmind_auth_service.application.service;

import com.opsmind_auth_service.application.dto.LogoutRequest;
import com.opsmind_auth_service.application.exception.BusinessException;
import com.opsmind_auth_service.application.usecase.LogoutUseCase;
import com.opsmind_auth_service.domain.entity.RefreshToken;
import com.opsmind_auth_service.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void execute(LogoutRequest request) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() ->
                        new BusinessException("Invalid refresh token")
                );

        if (refreshToken.isRevoked()) {
            // Token já revogado — logout silencioso (idempotente)
            return;
        }

        // Revoga todos os refresh tokens do usuário (logout global da sessão)
        refreshTokenRepository.revokeAllByUserId(refreshToken.getUserId());
    }
}
