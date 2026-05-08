package com.opsmind_company_service.application.port;

import com.opsmind.events.company.CompanyCreatedEvent;
import com.opsmind.events.company.InviteAcceptedEvent;
import com.opsmind.events.company.InviteCreatedEvent;
import com.opsmind.events.company.MemberRemovedEvent;

/**
 * Output port for company domain events.
 *
 * <p>Two adapters implement this interface:
 * <ul>
 *   <li>{@code KafkaCompanyEventAdapter} — active when {@code kafka.enabled=true}</li>
 *   <li>{@code NoOpCompanyEventAdapter}  — active when {@code kafka.enabled=false} (default)</li>
 * </ul>
 */
public interface CompanyEventPort {

    void publish(CompanyCreatedEvent event);

    void publish(InviteCreatedEvent event);

    void publish(InviteAcceptedEvent event);

    void publish(MemberRemovedEvent event);
}
