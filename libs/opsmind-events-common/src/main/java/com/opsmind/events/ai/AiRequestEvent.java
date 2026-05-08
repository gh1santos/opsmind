package com.opsmind.events.ai;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * Dispatched by the AI orchestrator to route a task to the appropriate worker.
 *
 * <p>Topic: {@code ai-requests}</p>
 * <p>Consumer: ai-worker (partitioned by model/capability)</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AiRequestEvent extends DomainEvent {

    public static final String EVENT_TYPE = "AI_REQUEST";

    /** Unique request identifier — also used as correlation key for the result. */
    private String  requestId;

    /** Requesting user email. */
    private String  userEmail;

    /** Target AI model. E.g. "gpt-4o", "claude-3-5-sonnet". */
    private String  model;

    /** Task type / capability. E.g. "SUMMARIZE", "CODE_REVIEW", "CHAT". */
    private String  taskType;

    /** Prompt or structured input payload (serialized JSON). */
    private String  payload;

    /** Additional model parameters (temperature, maxTokens, etc.). */
    private Map<String, String> parameters;

    public static AiRequestEvent of(String requestId, UUID tenantId, String userEmail,
                                     String model, String taskType, String payload,
                                     Map<String, String> parameters) {
        AiRequestEvent e = AiRequestEvent.builder()
                .requestId(requestId)
                .userEmail(userEmail)
                .model(model)
                .taskType(taskType)
                .payload(payload)
                .parameters(parameters)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
