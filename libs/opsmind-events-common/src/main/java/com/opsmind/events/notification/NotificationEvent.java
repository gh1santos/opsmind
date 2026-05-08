package com.opsmind.events.notification;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * Generic notification request published by any service.
 * The notification-service is the sole consumer — it resolves
 * the template and dispatches via email/push/webhook.
 *
 * <p>Topic: {@code notifications}</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent extends DomainEvent {

    public static final String EVENT_TYPE = "NOTIFICATION_REQUESTED";

    public enum Channel { EMAIL, PUSH, WEBHOOK, SMS }

    /** Recipient address (email / device token / webhook URL). */
    private String   recipient;

    /** Template identifier — resolved by notification-service. */
    private String   template;

    /** Preferred dispatch channel. */
    private Channel  channel;

    /**
     * Dynamic variables injected into the template.
     * E.g. {@code {"firstName": "Alice", "inviteUrl": "https://..."}}
     */
    private Map<String, String> variables;

    public static NotificationEvent of(UUID tenantId, String recipient,
                                        String template, Channel channel,
                                        Map<String, String> variables) {
        NotificationEvent e = NotificationEvent.builder()
                .recipient(recipient)
                .template(template)
                .channel(channel)
                .variables(variables)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
