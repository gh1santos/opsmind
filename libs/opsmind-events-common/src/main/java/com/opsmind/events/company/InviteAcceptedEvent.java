package com.opsmind.events.company;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Fired when a user accepts a company invite and becomes a member.
 * <p>Topic: {@code company-events}</p>
 * <p>Consumers: notification-service (welcome to team), billing-service (seat consumed), audit-service</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InviteAcceptedEvent extends DomainEvent {

    public static final String EVENT_TYPE = "INVITE_ACCEPTED";

    private Long   companyId;
    private Long   membershipId;
    private String userEmail;
    private String role;

    public static InviteAcceptedEvent of(Long companyId, UUID tenantId,
                                          Long membershipId, String userEmail, String role) {
        InviteAcceptedEvent e = InviteAcceptedEvent.builder()
                .companyId(companyId)
                .membershipId(membershipId)
                .userEmail(userEmail)
                .role(role)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
