package com.opsmind_company_service.application.usecase;

import com.opsmind_company_service.application.dto.request.UpdateCompanyRequest;
import com.opsmind_company_service.application.dto.response.CompanyResponse;

public interface UpdateCompanyUseCase {
    CompanyResponse execute(Long companyId, UpdateCompanyRequest request);
}
