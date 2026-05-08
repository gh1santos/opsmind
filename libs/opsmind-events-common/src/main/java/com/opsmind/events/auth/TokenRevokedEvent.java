package com.opsmind.events.auth;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
<parameter name="content">package com.opsmind.events.auth;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Fired when a user's refresh tokens are revoked (logout or theft detection).
 * <p>Topic: {@code auth-events}</p>
 * <p>Consumers: audit-service, security-service</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRevokedEvent extends DomainEvent {

    public static final String EVENT_TYPE = "TOKEN_REVOKED";

    public enum Reason { LOGOUT, TOKEN_THEFT_DETECTED, ADMIN_REVOKED }

    private Long   userId;
    private String email;
    private Reason reason;

    public static TokenRevokedEvent of(Long userId, UUID tenantId, String email, Reason reason) {
        TokenRevokedEvent e = TokenRevokedEvent.builder()
                .userId(userId)
                .email(email)
                .reason(reason)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
