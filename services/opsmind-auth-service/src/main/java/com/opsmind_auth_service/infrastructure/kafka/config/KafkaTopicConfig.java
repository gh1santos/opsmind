package com.opsmind_auth_service.infrastructure.kafka.config;

import com.opsmind.events.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Declares all Kafka topics owned or consumed by the auth-service.
 *
 * <p>Spring Kafka's {@link org.springframework.kafka.core.KafkaAdmin}
 * auto-creates topics from {@link NewTopic} beans on application startup
 * (idempotent — does nothing if the topic already exists).</p>
 *
 * <p>Partition counts:
 * <ul>
 *   <li>auth-events      — 3 partitions (moderate volume, keyed by userId)</li>
 *   <li>audit-events     — 6 partitions (high volume, all services write here)</li>
 *   <li>notifications    — 6 partitions (high volume, async dispatch)</li>
 * </ul>
 * Replication factor = 1 for local dev. Set via env var in production.
 */
@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
public class KafkaTopicConfig {

    @Bean
    public NewTopic authEventsTopic() {
        return TopicBuilder.name(KafkaTopics.AUTH_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic authEventsDltTopic() {
        return TopicBuilder.name(KafkaTopics.AUTH_EVENTS_DLT)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic auditEventsTopic() {
        return TopicBuilder.name(KafkaTopics.AUDIT_EVENTS)
                .partitions(6)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationsTopic() {
        return TopicBuilder.name(KafkaTopics.NOTIFICATIONS)
                .partitions(6)
                .replicas(1)
                .build();
    }
}
