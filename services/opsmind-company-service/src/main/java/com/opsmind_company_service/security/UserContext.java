package com.opsmind_company_service.security;

import java.util.UUID;

/**
 * Holder do usuário autenticado na thread corrente.
 * Populado pelo GatewaySecurityFilter a partir dos headers
 * X-User-Email, X-User-Role e X-Tenant-Id injetados pelo gateway.
 */
public final class UserContext {

    private static final ThreadLocal<UserPrincipal> CURRENT_USER =
            new InheritableThreadLocal<>();

    private UserContext() {}

    public static void setUser(UserPrincipal user) {
        CURRENT_USER.set(user);
    }

    public static UserPrincipal getUser() {
        return CURRENT_USER.get();
    }

    public static String getEmail() {
        UserPrincipal u = CURRENT_USER.get();
        return u != null ? u.email() : null;
    }

    public static String getRole() {
        UserPrincipal u = CURRENT_USER.get();
        return u != null ? u.role() : null;
    }

    public static UUID getTenantId() {
        UserPrincipal u = CURRENT_USER.get();
        return u != null ? u.tenantId() : null;
    }

    public static void clear() {
        CURRENT_USER.remove();
    }

    // ─────────────────────────────────────────────────────────────────────────

    public record UserPrincipal(String email, String role, UUID tenantId) {}
}
