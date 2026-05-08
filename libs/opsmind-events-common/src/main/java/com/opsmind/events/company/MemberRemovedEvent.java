package com.opsmind.events.company;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Fired when a member is removed (deactivated) from a company.
 * <p>Topic: {@code company-events}</p>
 * <p>Consumers: billing-service (seat released), audit-service</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRemovedEvent extends DomainEvent {

    public static final String EVENT_TYPE = "MEMBER_REMOVED";

    private Long   companyId;
    private String memberEmail;
    private String removedByEmail;

    public static MemberRemovedEvent of(Long companyId, UUID tenantId,
                                         String memberEmail, String removedByEmail) {
        MemberRemovedEvent e = MemberRemovedEvent.builder()
                .companyId(companyId)
                .memberEmail(memberEmail)
                .removedByEmail(removedByEmail)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
