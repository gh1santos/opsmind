package com.opsmind.events.auth;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Fired when a new user completes registration.
 * <p>Topic: {@code auth-events}</p>
 * <p>Consumers: notification-service (welcome email), audit-service, billing-service (start trial)</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent extends DomainEvent {

    public static final String EVENT_TYPE = "USER_REGISTERED";

    private Long   userId;
    private String email;
    private String firstName;
    private String lastName;

    public static UserRegisteredEvent of(Long userId, UUID tenantId,
                                         String email, String firstName, String lastName) {
        UserRegisteredEvent e = UserRegisteredEvent.builder()
                .userId(userId)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
