package com.opsmind_auth_service.security.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Proteção contra brute force em memória.
 * Bloqueia o identificador (email ou IP) após MAX_ATTEMPTS falhas
 * consecutivas dentro de WINDOW_MINUTES, por LOCK_MINUTES.
 *
 * Nota: ao escalar para múltiplas instâncias, substituir por
 * implementação Redis (já planejada na stack).
 */
@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS    = 5;
    private static final int WINDOW_MINUTES  = 15;
    private static final int LOCK_MINUTES    = 15;

    private final ConcurrentHashMap<String, LoginAttempt> attempts =
            new ConcurrentHashMap<>();

    /**
     * Registra um login bem-sucedido — limpa o contador do identificador.
     */
    public void registerSuccess(String identifier) {
        attempts.remove(identifier);
    }

    /**
     * Registra uma falha de login.
     */
    public void registerFailure(String identifier) {
        attempts.compute(identifier, (key, existing) -> {
            if (existing == null || existing.isWindowExpired()) {
                return new LoginAttempt(1, LocalDateTime.now());
            }
            return existing.increment();
        });
    }

    /**
     * Retorna true se o identificador estiver bloqueado.
     */
    public boolean isBlocked(String identifier) {
        LoginAttempt attempt = attempts.get(identifier);
        if (attempt == null) return false;

        if (attempt.isWindowExpired()) {
            attempts.remove(identifier);
            return false;
        }

        return attempt.count() >= MAX_ATTEMPTS;
    }

    /**
     * Retorna quantos minutos faltam para o desbloqueio (0 se não bloqueado).
     */
    public long minutesUntilUnblock(String identifier) {
        LoginAttempt attempt = attempts.get(identifier);
        if (attempt == null || attempt.isWindowExpired()) return 0;
        if (attempt.count() < MAX_ATTEMPTS) return 0;

        LocalDateTime unblockAt = attempt.firstAttemptAt()
                .plusMinutes(LOCK_MINUTES);

        long minutes = java.time.Duration.between(
                LocalDateTime.now(), unblockAt
        ).toMinutes();

        return Math.max(0, minutes);
    }

    // -------------------------------------------------------------------------

    private record LoginAttempt(int count, LocalDateTime firstAttemptAt) {

        LoginAttempt increment() {
            return new LoginAttempt(this.count + 1, this.firstAttemptAt);
        }

        boolean isWindowExpired() {
            return firstAttemptAt
                    .plusMinutes(WINDOW_MINUTES)
                    .isBefore(LocalDateTime.now());
        }
    }
}
