package com.opsmind_api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.justOrEmpty(
                        exchange.getRequest().getRemoteAddress()
                )
                .map(addr -> "ip:" + addr.getAddress().getHostAddress())
                .defaultIfEmpty("ip:unknown");
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String userEmail = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-User-Email");

            if (userEmail != null && !userEmail.isBlank()) {
                return Mono.just("user:" + userEmail);
            }

            return Mono.justOrEmpty(exchange.getRequest().getRemoteAddress())
                    .map(addr -> "ip:" + addr.getAddress().getHostAddress())
                    .defaultIfEmpty("ip:unknown");
        };
    }
}
