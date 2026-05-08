package com.opsmind_company_service.domain.enums;

public enum InviteStatus {
    PENDING,   // Aguardando aceite
    ACCEPTED,  // Aceito pelo usuário
    EXPIRED,   // Expirou sem ser aceito
    REVOKED    // Revogado manualmente
}
