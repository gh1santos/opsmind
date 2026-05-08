package com.opsmind_auth_service.application.service;

import com.opsmind_auth_service.application.dto.LoginResponse;
import com.opsmind_auth_service.application.dto.RefreshTokenRequest;
import com.opsmind_auth_service.application.exception.BusinessException;
import com.opsmind_auth_service.application.usecase.RefreshTokenUseCase;
import com.opsmind_auth_service.domain.entity.RefreshToken;
import com.opsmind_auth_service.domain.repository.RefreshTokenRepository;
import com.opsmind_auth_service.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public LoginResponse execute(RefreshTokenRequest request) {

        RefreshToken oldToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() ->
                        new BusinessException("Invalid refresh token")
                );

        if (oldToken.isRevoked()) {
            // Token já foi usado — possível roubo de token: revoga toda a família
            refreshTokenRepository.revokeAllByUserId(oldToken.getUserId());
            throw new BusinessException(
                    "Refresh token already used. All sessions revoked for security."
            );
        }

        if (oldToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.revokeByToken(oldToken.getToken());
            throw new BusinessException("Refresh token expired");
        }

        // Rotation: gera novo refresh token e revoga o anterior
        String newRefreshTokenValue = UUID.randomUUID().toString();

        String userRole = oldToken.getUserRole() != null
                ? oldToken.getUserRole()
                : "USER";

        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRefreshTokenValue)
                .userId(oldToken.getUserId())
                .userEmail(oldToken.getUserEmail())
                .userRole(userRole)
                .tenantId(oldToken.getTenantId())
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(newRefreshToken);

        // Revoga o token antigo após salvar o novo (ordem importa)
        refreshTokenRepository.revokeByToken(oldToken.getToken());

        // Gera novo access token com dados corretos do usuário
        String accessToken = jwtService.generateToken(
                oldToken.getUserEmail(),
                userRole,
                oldToken.getTenantId()
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .build();
    }
}
