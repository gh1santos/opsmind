package com.opsmind_company_service.security;

import java.util.UUID;

/**
 * Holder de contexto multi-tenant por thread (ThreadLocal).
 * Populado pelo GatewaySecurityFilter a partir do header X-Tenant-Id.
 * Deve ser limpo ao final de cada requisição (feito no finally do filtro).
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
