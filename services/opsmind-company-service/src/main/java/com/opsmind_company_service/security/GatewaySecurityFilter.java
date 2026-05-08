package com.opsmind_company_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Filtro de segurança baseado nos headers injetados pelo API Gateway.
 *
 * O gateway já validou o JWT e propagou:
 *   X-User-Email  → email do usuário autenticado
 *   X-User-Role   → role (USER, ADMIN)
 *   X-Tenant-Id   → UUID do tenant
 *
 * Este filtro:
 *   1. Lê esses headers
 *   2. Popula TenantContext e UserContext (ThreadLocal)
 *   3. Cria Authentication no SecurityContextHolder (suporte a @PreAuthorize)
 *   4. Limpa os contextos no finally (thread pool reutiliza threads)
 *
 * Nota de segurança: em ambiente de produção, adicionar validação de
 * um shared-secret (X-Gateway-Secret) para garantir que a requisição
 * passou pelo gateway e não chegou direto ao serviço.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewaySecurityFilter extends OncePerRequestFilter {

    private static final String USER_EMAIL_HEADER = "X-User-Email";
    private static final String USER_ROLE_HEADER  = "X-User-Role";
    private static final String TENANT_ID_HEADER  = "X-Tenant-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String userEmail   = request.getHeader(USER_EMAIL_HEADER);
            String userRole    = request.getHeader(USER_ROLE_HEADER);
            String tenantIdStr = request.getHeader(TENANT_ID_HEADER);

            if (userEmail != null && !userEmail.isBlank()) {

                UUID tenantId = parseTenantId(tenantIdStr);

                // Popula contextos de thread
                TenantContext.setTenantId(tenantId);
                UserContext.setUser(new UserContext.UserPrincipal(userEmail, userRole, tenantId));

                // Popula SecurityContext para suporte a @PreAuthorize
                String authority = "ROLE_" + (userRole != null ? userRole : "USER");
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userEmail,
                                null,
                                List.of(new SimpleGrantedAuthority(authority))
                        );
                SecurityContextHolder.getContext().setAuthentication(auth);

                log.debug("Security context set — user: {}, role: {}, tenant: {}",
                        userEmail, userRole, tenantId);
            }

            filterChain.doFilter(request, response);

        } finally {
            // Limpa sempre — evita vazamento entre threads do pool
            TenantContext.clear();
            UserContext.clear();
            SecurityContextHolder.clearContext();
        }
    }

    private UUID parseTenantId(String tenantIdStr) {
        if (tenantIdStr == null || tenantIdStr.isBlank()) return null;
        try {
            return UUID.fromString(tenantIdStr);
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid X-Tenant-Id header: {}", tenantIdStr);
            return null;
        }
    }
}
