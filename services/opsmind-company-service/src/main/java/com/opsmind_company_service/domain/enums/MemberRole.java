package com.opsmind_company_service.domain.enums;

public enum MemberRole {
    OWNER,   // Criador da empresa — acesso total, não pode ser removido
    ADMIN,   // Gerencia membros, planos e configurações
    MEMBER,  // Acesso padrão às features do plano
    VIEWER   // Apenas leitura
}
