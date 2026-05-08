package com.opsmind.events.billing;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Billing lifecycle events: plan changes, trial expiry, payment outcomes, seat adjustments.
 *
 * <p>Topic: {@code billing-events}</p>
 * <p>Consumer: billing-service, notification-service</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BillingEvent extends DomainEvent {

    public enum BillingEventType {
        TRIAL_STARTED,
        TRIAL_EXPIRED,
        SUBSCRIPTION_UPGRADED,
        SUBSCRIPTION_DOWNGRADED,
        SUBSCRIPTION_CANCELLED,
        PAYMENT_SUCCEEDED,
        PAYMENT_FAILED,
        SEAT_ADDED,
        SEAT_REMOVED
    }

    private Long             companyId;
    private String           ownerEmail;
    private BillingEventType billingEventType;
    private String           fromPlan;
    private String           toPlan;
    /** Amount in cents (BRL). Null for non-payment events. */
    private Long             amountCents;

    public static BillingEvent of(Long companyId, UUID tenantId, String ownerEmail,
                                   BillingEventType billingEventType,
                                   String fromPlan, String toPlan, Long amountCents) {
        BillingEvent e = BillingEvent.builder()
                .companyId(companyId)
                .ownerEmail(ownerEmail)
                .billingEventType(billingEventType)
                .fromPlan(fromPlan)
                .toPlan(toPlan)
                .amountCents(amountCents)
                .build();
        initBase(e, billingEventType.name(), tenantId);
        return e;
    }
}
