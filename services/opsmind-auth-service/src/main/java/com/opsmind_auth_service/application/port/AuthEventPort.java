package com.opsmind_auth_service.application.port;

import com.opsmind.events.auth.TokenRevokedEvent;
import com.opsmind.events.auth.UserLoggedInEvent;
import com.opsmind.events.auth.UserRegisteredEvent;

/**
 * Output port for auth domain events.
 *
 * <p>Two adapters implement this interface:
 * <ul>
 *   <li>{@code KafkaAuthEventAdapter} — active when {@code kafka.enabled=true}</li>
 *   <li>{@code NoOpAuthEventAdapter}  — active when {@code kafka.enabled=false} (default/dev)</li>
 * </ul>
 *
 * <p>Application services depend only on this interface —
 * they are completely decoupled from Kafka internals.
 */
public interface AuthEventPort {

    void publish(UserRegisteredEvent event);

    void publish(UserLoggedInEvent event);

    void publish(TokenRevokedEvent event);
}
