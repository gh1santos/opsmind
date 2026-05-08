package com.opsmind_auth_service.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RefreshToken {

    private Long id;

    private String token;

    private Long userId;

    private boolean revoked;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;
}