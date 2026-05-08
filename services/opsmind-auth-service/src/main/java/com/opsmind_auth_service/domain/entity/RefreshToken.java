package com.opsmind_auth_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    private Long id;

    private String token;

    private Long userId;

    private String userEmail;

    private String userRole;

    private UUID tenantId;

    private String replacedByToken;

    private boolean revoked;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;
}