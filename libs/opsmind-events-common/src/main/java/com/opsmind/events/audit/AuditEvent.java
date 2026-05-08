package com.opsmind.events.audit;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Immutable audit trail event for compliance and security review.
 * Every write operation across all services should produce one.
 *
 * <p>Topic: {@code audit-events}</p>
 * <p>Consumer: audit-service (persists to append-only store)</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent extends DomainEvent {

    public static final String EVENT_TYPE = "AUDIT";

    /** Service that produced the event. E.g. "auth-service", "company-service". */
    private String service;

    /** Action performed. E.g. "USER_LOGIN", "INVITE_CREATED", "MEMBER_REMOVED". */
    private String action;

    /** Resource type affected. E.g. "User", "Company", "Membership". */
    private String resourceType;

    /** Resource identifier (could be ID or email). */
    private String resourceId;

    /** Actor who triggered the action (email or system). */
    private String actor;

    /** HTTP status code of the operation, if applicable. */
    private Integer statusCode;

    /** Optional extra context (serialized JSON snippet or short description). */
    private String detail;

    public static AuditEvent of(String service, String action, UUID tenantId,
                                 String resourceType, String resourceId,
                                 String actor, Integer statusCode, String detail) {
        AuditEvent e = AuditEvent.builder()
                .service(service)
                .action(action)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .actor(actor)
                .statusCode(statusCode)
                .detail(detail)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
