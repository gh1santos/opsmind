package com.opsmind_api_gateway.filter;

import com.opsmind_api_gateway.properties.GatewayProperties;
import com.opsmind_api_gateway.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX         = "Bearer ";

    // Headers propagados ao downstream
    public static final String USER_EMAIL_HEADER  = "X-User-Email";
    public static final String USER_ROLE_HEADER   = "X-User-Role";
    public static final String TENANT_ID_HEADER   = "X-Tenant-Id";

    private final JwtService jwtService;
    private final GatewayProperties gatewayProperties;

    @Override
    public int getOrder() {
        return -90;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (gatewayProperties.isPublicPath(path)) {
            log.debug("[{}] Public path — skipping JWT: {}",
                    getCorrelationId(exchange), path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("[{}] Missing or malformed Authorization header for path: {}",
                    getCorrelationId(exchange), path);
            return respondUnauthorized(exchange, "Authorization header missing or invalid");
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        if (!jwtService.isTokenValid(token)) {
            log.warn("[{}] Invalid or expired JWT for path: {}",
                    getCorrelationId(exchange), path);
            return respondUnauthorized(exchange, "Invalid or expired token");
        }

        String email    = jwtService.extractEmail(token);
        String role     = jwtService.extractRole(token);
        String tenantId = jwtService.extractTenantId(token);

        log.debug("[{}] JWT valid — user: {}, role: {}, tenant: {}",
                getCorrelationId(exchange), email, role, tenantId);

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(USER_EMAIL_HEADER, email  != null ? email    : "")
                .header(USER_ROLE_HEADER,  role   != null ? role     : "")
                .header(TENANT_ID_HEADER,  tenantId != null ? tenantId : "")
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private Mono<Void> respondUnauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
                "{\"success\":false,\"message\":\"%s\",\"code\":\"UNAUTHORIZED\"}",
                message
        );

        DataBuffer buffer = response.bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }

    private String getCorrelationId(ServerWebExchange exchange) {
        String id = exchange.getRequest()
                .getHeaders()
                .getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER);
        return id != null ? id : "no-correlation-id";
    }
}
