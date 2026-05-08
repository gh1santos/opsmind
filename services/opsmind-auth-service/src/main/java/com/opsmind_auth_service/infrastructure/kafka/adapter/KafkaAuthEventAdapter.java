package com.opsmind_auth_service.infrastructure.kafka.adapter;

import com.opsmind.events.KafkaTopics;
import com.opsmind.events.auth.TokenRevokedEvent;
import com.opsmind.events.auth.UserLoggedInEvent;
import com.opsmind.events.auth.UserRegisteredEvent;
import com.opsmind_auth_service.application.port.AuthEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka adapter for the {@link AuthEventPort}.
 *
 * <p>Active only when {@code kafka.enabled=true} in application.yml.
 * Key = eventId (enables idempotency on the consumer side via partitioning).
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
public class KafkaAuthEventAdapter implements AuthEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(UserRegisteredEvent event) {
        send(KafkaTopics.AUTH_EVENTS, event.getEventId(), event);
    }

    @Override
    public void publish(UserLoggedInEvent event) {
        send(KafkaTopics.AUTH_EVENTS, event.getEventId(), event);
    }

    @Override
    public void publish(TokenRevokedEvent event) {
        send(KafkaTopics.AUTH_EVENTS, event.getEventId(), event);
    }

    private void send(String topic, String key, Object payload) {
        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, key, payload);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[Kafka] Failed to publish to topic={} key={}: {}",
                        topic, key, ex.getMessage(), ex);
            } else {
                log.debug("[Kafka] Published to topic={} partition={} offset={} key={}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        key);
            }
        });
    }
}
