package com.opsmind_company_service.infrastructure.kafka.config;

import com.opsmind.events.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Topic declarations for the company-service.
 *
 * <p>Partition strategy:
 * <ul>
 *   <li>company-events  — 3 partitions, keyed by tenantId</li>
 *   <li>notifications   — 6 partitions, high-volume async dispatch</li>
 *   <li>billing-events  — 3 partitions</li>
 * </ul>
 */
@Configuration
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
public class KafkaTopicConfig {

    @Bean
    public NewTopic companyEventsTopic() {
        return TopicBuilder.name(KafkaTopics.COMPANY_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic companyEventsDltTopic() {
        return TopicBuilder.name(KafkaTopics.COMPANY_EVENTS_DLT)
                .partitions(3)
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

    @Bean
    public NewTopic billingEventsTopic() {
        return TopicBuilder.name(KafkaTopics.BILLING_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
