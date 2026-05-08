package com.opsmind_api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * Configuração dos KeyResolvers para o rate limiting do Spring Cloud Gateway.
 *
 * Os nomes dos beans são referenciados no application.yml via SpEL:
 *   key-resolver: "#{@ipKeyResolver}"
 *   key-resolver: "#{@userKeyResolver}"
 */
@Configuration
public class GatewayRoutesConfig {

    /**
     * Rate limiting por IP — usado em rotas públicas.
     * Protege /register, /login, /refresh contra bots e brute force.
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.justOrEmpty(
                        exchange.getRequest().getRemoteAddress()
                )
                .map(addr -> "ip:" + addr.getAddress().getHostAddress())
                .defaultIfEmpty("ip:unknown");
    }

    /**
     * Rate limiting por usuário autenticado — usado em rotas protegidas.
     * Lê o header X-User-Email injetado pelo JwtAuthenticationFilter.
     * Fallback para IP se o header não estiver presente.
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String userEmail = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-User-Email");

            if (userEmail != null && !userEmail.isBlank()) {
                return Mono.just("user:" + userEmail);
            }

            // Fallback para IP se JWT não foi processado ainda
            return Mono.justOrEmpty(exchange.getRequest().getRemoteAddress())
                    .map(addr -> "ip:" + addr.getAddress().getHostAddress())
                    .defaultIfEmpty("ip:unknown");
        };
    }
}
