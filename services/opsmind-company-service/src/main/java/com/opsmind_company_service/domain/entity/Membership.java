package com.opsmind_company_service.domain.entity;

import com.opsmind_company_service.domain.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    private Long id;

    private Long companyId;

    /** TenantId da empresa (para isolamento multi-tenant) */
    private UUID tenantId;

    /**
     * Email do usuário — referência cross-service.
     * Não é FK para a tabela users (está em outro banco/serviço).
     */
    private String userEmail;

    private MemberRole role;

    private Boolean active;

    private LocalDateTime joinedAt;

    private LocalDateTime updatedAt;
}
