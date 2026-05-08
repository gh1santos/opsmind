package com.opsmind_company_service.application.usecase;

import com.opsmind_company_service.application.dto.response.CompanyResponse;

import java.util.UUID;

public interface GetCompanyUseCase {
    CompanyResponse getById(Long id);
    CompanyResponse getByTenantId(UUID tenantId);
    CompanyResponse getMyCompany();
}
