package com.opsmind.events.auth;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Fired on every successful login.
 * <p>Topic: {@code auth-events}</p>
 * <p>Consumers: audit-service (access log), security-service (anomaly detection)</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoggedInEvent extends DomainEvent {

    public static final String EVENT_TYPE = "USER_LOGGED_IN";

    private Long   userId;
    private String email;
    private String role;
    /** Source IP extracted from X-Forwarded-For — may be null in internal calls. */
    private String ipAddress;

    public static UserLoggedInEvent of(Long userId, UUID tenantId,
                                        String email, String role, String ipAddress) {
        UserLoggedInEvent e = UserLoggedInEvent.builder()
                .userId(userId)
                .email(email)
                .role(role)
                .ipAddress(ipAddress)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
