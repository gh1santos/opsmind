package com.opsmind.events.company;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Fired when a new company (tenant) is created.
 * <p>Topic: {@code company-events}</p>
 * <p>Consumers: billing-service (start free trial), notification-service (onboarding email), audit-service</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreatedEvent extends DomainEvent {

    public static final String EVENT_TYPE = "COMPANY_CREATED";

    private Long   companyId;
    private String companyName;
    private String slug;
    private String ownerEmail;
    private String planType;

    public static CompanyCreatedEvent of(Long companyId, UUID tenantId,
                                          String companyName, String slug,
                                          String ownerEmail, String planType) {
        CompanyCreatedEvent e = CompanyCreatedEvent.builder()
                .companyId(companyId)
                .companyName(companyName)
                .slug(slug)
                .ownerEmail(ownerEmail)
                .planType(planType)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
