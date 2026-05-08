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

        String token =
                jwtService.generateToken(user.getEmail());

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .build();
    }
}
