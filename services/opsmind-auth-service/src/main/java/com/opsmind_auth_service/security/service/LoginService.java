package com.opsmind_auth_service.security.service;

import com.opsmind.events.auth.UserLoggedInEvent;
import com.opsmind_auth_service.application.dto.LoginRequest;
import com.opsmind_auth_service.application.dto.LoginResponse;
import com.opsmind_auth_service.application.exception.BusinessException;
import com.opsmind_auth_service.application.port.AuthEventPort;
import com.opsmind_auth_service.application.usecase.LoginUseCase;
import com.opsmind_auth_service.domain.entity.RefreshToken;
import com.opsmind_auth_service.domain.repository.RefreshTokenRepository;
import com.opsmind_auth_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final UserRepository          userRepository;
    private final RefreshTokenRepository  refreshTokenRepository;
    private final PasswordEncoder         passwordEncoder;
    private final JwtService              jwtService;
    private final LoginAttemptService     loginAttemptService;
    private final AuthEventPort           authEventPort;

    @Override
    public LoginResponse execute(LoginRequest request) {

        String identifier = request.getEmail();

        // Brute Force: bloqueia se excedeu tentativas
        if (loginAttemptService.isBlocked(identifier)) {
            long minutesLeft = loginAttemptService.minutesUntilUnblock(identifier);
            throw new BusinessException(
                    "Account temporarily locked. Try again in " + minutesLeft + " minute(s).");
        }

        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    loginAttemptService.registerFailure(identifier);
                    return new BusinessException("Invalid credentials");
                });

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(), user.getPasswordHash());

        if (!passwordMatches) {
            loginAttemptService.registerFailure(identifier);
            throw new BusinessException("Invalid credentials");
        }

        // Login bem-sucedido: limpa contador de falhas
        loginAttemptService.registerSuccess(identifier);

        String role = user.getRoles()
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException("User has no role assigned"))
                .getName()
                .name();

        // Gera access token com tenantId no claim
        String accessToken = jwtService.generateToken(user.getEmail(), role, user.getTenantId());

        // Gera e persiste refresh token
        String refreshTokenValue = UUID.randomUUID().toString();
        refreshTokenRepository.save(RefreshToken.builder()
                .token(refreshTokenValue)
                .userId(user.getId())
                .userEmail(user.getEmail())
                .userRole(role)
                .tenantId(user.getTenantId())
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build());

        // Publica evento de login — IP não disponível no service (camada application)
        // O controller pode enriquecer via header X-Forwarded-For se necessário
        authEventPort.publish(UserLoggedInEvent.of(
                user.getId(),
                user.getTenantId(),
                user.getEmail(),
                role,
                null  // ipAddress — enriquecido futuramente via request header
        ));

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .build();
    }
}
