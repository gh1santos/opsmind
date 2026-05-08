package com.opsmind_auth_service.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RegisterUserResponse {

    private Long userId;

    private UUID tenantId;

    private String email;

    private String message;
}
