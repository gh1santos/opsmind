package com.opsmind.events.ai;

import com.opsmind.events.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Published by the AI worker after processing a request.
 *
 * <p>Topic: {@code ai-results}</p>
 * <p>Consumer: ai-orchestrator (routes result back to caller via WebSocket / SSE)</p>
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AiResultEvent extends DomainEvent {

    public static final String EVENT_TYPE = "AI_RESULT";

    public enum Status { SUCCESS, FAILED, TIMEOUT, RATE_LIMITED }

    /** Matches the {@code requestId} in {@link AiRequestEvent}. */
    private String  requestId;

    /** Execution status. */
    private Status  status;

    /** Model that processed the request. */
    private String  model;

    /** Result payload — serialized JSON or plain text depending on taskType. */
    private String  result;

    /** Error message if status != SUCCESS. */
    private String  errorMessage;

    /** Token usage for billing purposes. */
    private Integer inputTokens;
    private Integer outputTokens;

    /** Wall-clock processing duration in milliseconds. */
    private Long    durationMs;

    public static AiResultEvent of(String requestId, UUID tenantId, Status status,
                                    String model, String result, String errorMessage,
                                    Integer inputTokens, Integer outputTokens, Long durationMs) {
        AiResultEvent e = AiResultEvent.builder()
                .requestId(requestId)
                .status(status)
                .model(model)
                .result(result)
                .errorMessage(errorMessage)
                .inputTokens(inputTokens)
                .outputTokens(outputTokens)
                .durationMs(durationMs)
                .build();
        initBase(e, EVENT_TYPE, tenantId);
        return e;
    }
}
