package com.opsmind_auth_service.infrastructure.kafka.adapter;

import com.opsmind.events.auth.TokenRevokedEvent;
import com.opsmind.events.auth.UserLoggedInEvent;
import com.opsmind.events.auth.UserRegisteredEvent;
import com.opsmind_auth_service.application.port.AuthEventPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * No-op adapter used when Kafka is disabled ({@code kafka.enabled=false}).
 *
 * <p>Logs events at DEBUG level so developers can trace the event flow
 * without needing a real broker running locally.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpAuthEventAdapter implements AuthEventPort {

    @Override
    public void publish(UserRegisteredEvent event) {
        log.debug("[NoOp-Kafka] AUTH_EVENT: {} tenantId={} userId={} email={}",
                event.getEventType(), event.getTenantId(),
                event.getUserId(), event.getEmail());
    }

    @Override
    public void publish(UserLoggedInEvent event) {
        log.debug("[NoOp-Kafka] AUTH_EVENT: {} tenantId={} userId={} email={}",
                event.getEventType(), event.getTenantId(),
                event.getUserId(), event.getEmail());
    }

    @Override
    public void publish(TokenRevokedEvent event) {
        log.debug("[NoOp-Kafka] AUTH_EVENT: {} tenantId={} userId={} reason={}",
                event.getEventType(), event.getTenantId(),
                event.getUserId(), event.getReason());
    }
}
