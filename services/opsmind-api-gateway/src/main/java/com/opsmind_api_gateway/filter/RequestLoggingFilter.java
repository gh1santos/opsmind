package com.opsmind_api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtro global — Structured Request/Response Logging.
 * Ordem: Ordered.LOWEST_PRECEDENCE (último da cadeia de entrada, primeiro da saída).
 *
 * Log de entrada:  --> METHOD /path [correlation-id] from IP
 * Log de saída:    <-- STATUS METHOD /path [correlation-id] Xms
 *
 * Usa o X-Correlation-Id injetado pelo CorrelationIdFilter para rastreamento.
 */
@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String correlationId = getCorrelationId(exchange);
        long startTime = System.currentTimeMillis();

        String remoteAddress = request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress()
                : "unknown";

        log.info("--> {} {} [{}] from {}",
                request.getMethod(),
                request.getURI().getPath(),
                correlationId,
                remoteAddress
        );

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    long durationMs = System.currentTimeMillis() - startTime;

                    String statusCode = exchange.getResponse().getStatusCode() != null
                            ? exchange.getResponse().getStatusCode().toString()
                            : "UNKNOWN";

                    log.info("<-- {} {} {} [{}] {}ms",
                            statusCode,
                            request.getMethod(),
                            request.getURI().getPath(),
                            correlationId,
                            durationMs
                    );
                })
        );
    }

    private String getCorrelationId(ServerWebExchange exchange) {
        String id = exchange.getRequest()
                .getHeaders()
                .getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER);
        return id != null ? id : "no-correlation-id";
    }
}
