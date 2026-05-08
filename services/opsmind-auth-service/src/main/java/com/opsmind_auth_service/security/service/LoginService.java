package com.opsmind_auth_service.security.service;

import com.opsmind_auth_service.application.dto.LoginRequest;
import com.opsmind_auth_service.application.dto.LoginResponse;
import com.opsmind_auth_service.application.exception.BusinessException;
import com.opsmind_auth_service.application.usecase.LoginUseCase;
import com.opsmind_auth_service.domain.repository.UserRepository;
import com.opsmind_auth_service.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.opsmind_auth_service.domain.entity.RefreshToken;
import com.opsmind_auth_service.domain.repository.RefreshTokenRepository;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public LoginResponse execute(LoginRequest request) {

        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BusinessException("Invalid credentials")
                );

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPasswordHash()
                );

        if (!passwordMatches) {
            throw new BusinessException("Invalid credentials");
        }

        String role =
                user.getRoles()
                        .stream()
                        .findFirst()
                        .orElseThrow()
                        .getName()
                        .name();

        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        role
                );

        String refreshTokenValue =
                java.util.UUID.randomUUID().toString();

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(refreshTokenValue)
                        .userId(user.getId())
                        .revoked(false)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(
                                LocalDateTime.now().plusDays(7)
                        )
                        .build();

        refreshTokenRepository.save(refreshToken);

        return LoginResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .build();
    }
}
