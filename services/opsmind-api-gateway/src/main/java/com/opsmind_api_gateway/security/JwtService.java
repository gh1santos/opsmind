package com.opsmind_api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.UUID;

/**
 * Serviço de validação JWT do gateway.
 *
 * Apenas lê e valida tokens — nunca os gera (responsabilidade do auth-service).
 * Usa o mesmo secret HMAC-SHA256 que o auth-service para verificar a assinatura.
 */
@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Retorna true se o token for válido (assinatura + expiração).
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception ex) {
            log.debug("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Extrai o email do usuário (subject do JWT).
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrai o role do usuário (claim 'role').
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /**
     * Extrai o tenantId (claim 'tenantId') como String.
     * Retorna null se o claim não existir.
     */
    public String extractTenantId(String token) {
        return extractAllClaims(token).get("tenantId", String.class);
    }

    /**
     * Extrai o tenantId como UUID.
     * Retorna null se o claim não existir ou for inválido.
     */
    public UUID extractTenantIdAsUUID(String token) {
        String tenantIdStr = extractTenantId(token);
        if (tenantIdStr == null || tenantIdStr.isBlank()) return null;
        try {
            return UUID.fromString(tenantIdStr);
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid tenantId format in JWT: {}", tenantIdStr);
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
