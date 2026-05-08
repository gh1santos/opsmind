package com.opsmind_company_service.infrastructure.kafka.adapter;

import com.opsmind.events.KafkaTopics;
import com.opsmind.events.company.CompanyCreatedEvent;
import com.opsmind.events.company.InviteAcceptedEvent;
import com.opsmind.events.company.InviteCreatedEvent;
import com.opsmind.events.company.MemberRemovedEvent;
import com.opsmind_company_service.application.port.CompanyEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka adapter for {@link CompanyEventPort}.
 * Active only when {@code kafka.enabled=true}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
public class KafkaCompanyEventAdapter implements CompanyEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(CompanyCreatedEvent event) {
        send(KafkaTopics.COMPANY_EVENTS, event.getEventId(), event);
    }

    @Override
    public void publish(InviteCreatedEvent event) {
        send(KafkaTopics.COMPANY_EVENTS, event.getEventId(), event);
        // Replica também para notifications — o notification-service consome daí
        send(KafkaTopics.NOTIFICATIONS, event.getEventId(), event);
    }

    @Override
    public void publish(InviteAcceptedEvent event) {
        send(KafkaTopics.COMPANY_EVENTS, event.getEventId(), event);
    }

    @Override
    public void publish(MemberRemovedEvent event) {
        send(KafkaTopics.COMPANY_EVENTS, event.getEventId(), event);
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
