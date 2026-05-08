package com.opsmind_company_service.domain.entity;

import com.opsmind_company_service.domain.enums.CompanyIndustry;
import com.opsmind_company_service.domain.enums.CompanyStatus;
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
public class Company {

    private Long id;

    /** Identificador único de tenant — propaga para todos os recursos da empresa */
    private UUID tenantId;

    private String name;

    /** Slug URL-friendly único: ex. "opsmind-ai" */
    private String slug;

    /** CNPJ ou CPF (texto simples, validação futura) */
    private String document;

    private CompanyIndustry industry;

    private CompanyStatus status;

    /** Email do usuário que criou a empresa (owner) */
    private String ownerEmail;

    private Long planId;

    private LocalDateTime trialEndsAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
