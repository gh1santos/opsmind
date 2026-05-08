package com.opsmind_auth_service.application.service;

import com.opsmind.events.auth.UserRegisteredEvent;
import com.opsmind_auth_service.application.dto.RegisterUserRequest;
import com.opsmind_auth_service.application.dto.RegisterUserResponse;
import com.opsmind_auth_service.application.exception.BusinessException;
import com.opsmind_auth_service.application.port.AuthEventPort;
import com.opsmind_auth_service.application.usecase.RegisterUserUseCase;
import com.opsmind_auth_service.domain.entity.Role;
import com.opsmind_auth_service.domain.entity.User;
import com.opsmind_auth_service.domain.enums.UserRole;
import com.opsmind_auth_service.domain.repository.RoleRepository;
import com.opsmind_auth_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository  userRepository;
    private final RoleRepository  roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthEventPort   authEventPort;

    @Override
    @Transactional
    public RegisterUserResponse execute(RegisterUserRequest request) {

        validateEmail(request.getEmail());

        Role defaultRole = roleRepository
                .findByName(UserRole.USER)
                .orElseThrow(() -> new BusinessException("Default role not found"));

        UUID tenantId = UUID.randomUUID();

        User user = User.builder()
                .tenantId(tenantId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .roles(Set.of(defaultRole))
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        // Publica evento — desacoplado via porta (Kafka real ou NoOp em dev)
        authEventPort.publish(UserRegisteredEvent.of(
                savedUser.getId(),
                savedUser.getTenantId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName()
        ));

        return RegisterUserResponse.builder()
                .userId(savedUser.getId())
                .tenantId(savedUser.getTenantId())
                .email(savedUser.getEmail())
                .message("User registered successfully")
                .build();
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email already registered");
        }
    }
}
