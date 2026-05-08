package com.opsmind_api_gateway.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Controlador de fallback para os CircuitBreakers das rotas do gateway.
 * Retorna resposta amigável quando um serviço downstream está indisponível.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/auth")
    public Mono<ResponseEntity<Map<String, Object>>> authFallback() {
        Map<String, Object> body = Map.of(
                "success", false,
                "message", "Auth service is temporarily unavailable. Please try again later.",
                "code", "SERVICE_UNAVAILABLE"
        );
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(body));
    }

    @RequestMapping("/default")
    public Mono<ResponseEntity<Map<String, Object>>> defaultFallback() {
        Map<String, Object> body = Map.of(
                "success", false,
                "message", "Service temporarily unavailable. Please try again later.",
                "code", "SERVICE_UNAVAILABLE"
        );
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(body));
    }
}
