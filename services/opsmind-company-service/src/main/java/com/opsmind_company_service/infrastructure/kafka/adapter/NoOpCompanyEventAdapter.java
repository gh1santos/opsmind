package com.opsmind_company_service.infrastructure.kafka.adapter;

import com.opsmind.events.company.CompanyCreatedEvent;
import com.opsmind.events.company.InviteAcceptedEvent;
import com.opsmind.events.company.InviteCreatedEvent;
import com.opsmind.events.company.MemberRemovedEvent;
import com.opsmind_company_service.application.port.CompanyEventPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * No-op adapter — logs events at DEBUG when Kafka is disabled.
 * Default active profile (kafka.enabled=false or unset).
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpCompanyEventAdapter implements CompanyEventPort {

    @Override
    public void publish(CompanyCreatedEvent event) {
        log.debug("[NoOp-Kafka] COMPANY_EVENT: {} tenantId={} companyId={} slug={}",
                event.getEventType(), event.getTenantId(),
                event.getCompanyId(), event.getSlug());
    }

    @Override
    public void publish(InviteCreatedEvent event) {
        log.debug("[NoOp-Kafka] COMPANY_EVENT: {} tenantId={} invitedEmail={} role={}",
                event.getEventType(), event.getTenantId(),
                event.getInvitedEmail(), event.getRole());
    }

    @Override
    public void publish(InviteAcceptedEvent event) {
        log.debug("[NoOp-Kafka] COMPANY_EVENT: {} tenantId={} membershipId={} userEmail={}",
                event.getEventType(), event.getTenantId(),
                event.getMembershipId(), event.getUserEmail());
    }

    @Override
    public void publish(MemberRemovedEvent event) {
        log.debug("[NoOp-Kafka] COMPANY_EVENT: {} tenantId={} memberEmail={} removedBy={}",
                event.getEventType(), event.getTenantId(),
                event.getMemberEmail(), event.getRemovedByEmail());
    }
}
