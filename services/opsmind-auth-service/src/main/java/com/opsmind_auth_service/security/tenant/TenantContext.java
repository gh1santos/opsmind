package com.opsmind_auth_service.security.tenant;

import java.util.UUID;

/**
 * Holder de contexto multi-tenant por thread (ThreadLocal).
 * Deve ser limpo ao final de cada requisição HTTP.
 */
public final class TenantContext {

    private static final ThreadLocal<UUID> CURRENT_TENANT =
            new InheritableThreadLocal<>();

    private TenantContext() {}

    public static void setTenantId(UUID tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static UUID getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
