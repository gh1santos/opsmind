package com.opsmind.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for all domain events published to Kafka.
 *
 * <p>Fields common to every event:
 * <ul>
 *   <li>{@code eventId}   — unique UUID per event instance (idempotency key for consumers)</li>
 *   <li>{@code eventType} — discriminator string (e.g. "USER_REGISTERED")</li>
 *   <li>{@code occurredAt} — UTC instant when the event happened</li>
 *   <li>{@code tenantId}  — multi-tenant isolation (consumers must scope queries to this)</li>
 *   <li>{@code version}   — schema version for forward compatibility</li>
 * </ul>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class DomainEvent {

    /** Unique identifier for this event instance. */
    private String eventId;

    /** Discriminator — maps to a specific event sub-type. */
    private String eventType;

    /** Tenant that originated the event. */
    private UUID tenantId;

    /** UTC instant the event occurred. */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant occurredAt;

    /** Schema version — increment when breaking changes are introduced. */
    private int version = 1;

    /** Factory helper — populates eventId and occurredAt automatically. */
    protected static <T extends DomainEvent> void initBase(DomainEvent event,
                                                           String eventType,
                                                           UUID tenantId) {
        event.eventId    = UUID.randomUUID().toString();
        event.eventType  = eventType;
        event.tenantId   = tenantId;
        event.occurredAt = Instant.now();
        event.version    = 1;
    }
}
