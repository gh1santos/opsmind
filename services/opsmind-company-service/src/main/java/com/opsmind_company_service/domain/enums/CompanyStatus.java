package com.opsmind_company_service.domain.enums;

public enum CompanyStatus {
    TRIAL,      // Período gratuito de avaliação
    ACTIVE,     // Empresa ativa com plano pago ou free
    SUSPENDED,  // Suspensa por inadimplência ou violação
    INACTIVE    // Desativada pelo próprio owner
}
