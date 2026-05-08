package com.opsmind_auth_service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshTokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Long userId;

    private boolean revoked;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;
}