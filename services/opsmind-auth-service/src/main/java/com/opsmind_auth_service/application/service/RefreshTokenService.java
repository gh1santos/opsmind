package com.opsmind_auth_service.application.service;

import com.opsmind_auth_service.application.dto.LoginResponse;
import com.opsmind_auth_service.application.dto.RefreshTokenRequest;
import com.opsmind_auth_service.application.exception.BusinessException;
import com.opsmind_auth_service.application.usecase.RefreshTokenUseCase;
import com.opsmind_auth_service.domain.repository.RefreshTokenRepository;
import com.opsmind_auth_service.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;

    @Override
    public LoginResponse execute(
            RefreshTokenRequest request
    ) {

        var refreshToken =
                refreshTokenRepository
                        .findByToken(request.getRefreshToken())
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Invalid refresh token"
                                )
                        );

        if (refreshToken.isRevoked()) {
            throw new BusinessException(
                    "Refresh token revoked"
            );
        }

        if (refreshToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new BusinessException(
                    "Refresh token expired"
            );
        }

        String accessToken =
                jwtService.generateToken(
                        "user",
                        "USER"
                );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(86400L)
                .build();
    }
}
