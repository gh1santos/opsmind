package com.opsmind.events;

/**
 * Central registry of all Kafka topic names used across OpsMind microservices.
 *
 * <p>All services must import this class — never hard-code topic names
 * in individual services to avoid silent mismatches between producers
 * and consumers.</p>
 *
 * <p>Topic naming convention: {@code <domain>-events} for domain events,
 * {@code <name>} for purpose-specific topics.</p>
 */
public final class KafkaTopics {

    private KafkaTopics() {}

    // ── Domain event topics ──────────────────────────────────────────────────

    /** Authentication domain events: registration, login, logout, token revocation. */
    public static final String AUTH_EVENTS = "auth-events";

    /** Company domain events: company lifecycle, membership changes, invites. */
    public static final String COMPANY_EVENTS = "company-events";

    // ── Purpose-specific topics ───────────────────────────────────────────────

    /** Outbound notifications to be dispatched by the notification-service. */
    public static final String NOTIFICATIONS = "notifications";

    /** Audit trail events — immutable log for compliance and security review. */
    public static final String AUDIT_EVENTS = "audit-events";

    /** AI request payloads sent from the orchestrator to workers. */
    public static final String AI_REQUESTS = "ai-requests";

    /** AI result payloads returned from workers to the orchestrator. */
    public static final String AI_RESULTS = "ai-results";

    /** Billing lifecycle events: subscriptions, upgrades, payment outcomes. */
    public static final String BILLING_EVENTS = "billing-events";

    // ── Dead Letter Topics (DLT) ─────────────────────────────────────────────

    public static final String AUTH_EVENTS_DLT      = "auth-events.DLT";
    public static final String COMPANY_EVENTS_DLT   = "company-events.DLT";
    public static final String NOTIFICATIONS_DLT    = "notifications.DLT";
    public static final String AUDIT_EVENTS_DLT     = "audit-events.DLT";
    public static final String AI_REQUESTS_DLT      = "ai-requests.DLT";
}
