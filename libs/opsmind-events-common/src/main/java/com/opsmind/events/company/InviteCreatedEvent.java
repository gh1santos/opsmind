package com.opsmind.events.company;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

/**
 * Fired when a user invite is created.
 * <p>Topic: {@code company-events}</p>
 * <p>Consumers: notification-service (invite email), audit-service</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InviteCreatedEvent extends DomainEvent {

    public static final String EVENT_TYPE = "INVITE_CREATED";

    private Long    companyId;
    private String  companyName;
    private Long    inviteId;
    private String  invitedEmail;
    private String  invitedByEmail;
    private String  role;
    private String  token;
    private Instant expiresAt;

    public static InviteCreatedEvent of(Long companyId, String companyName, UUID tenantId,
                                         Long inviteId, String invitedEmail,
                                         String invitedByEmail, String role,
                                         String token, Instant expiresAt) {
        InviteCreatedEvent e = InviteCreatedEvent.builder()
                .companyId(companyId)
                .companyName(companyName)
                .inviteId(inviteId)
                .invitedEmail(invitedEmail)
                .invitedByEmail(invitedByEmail)
                .role(role)
                .token(token)
                .expiresAt(expiresAt)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
