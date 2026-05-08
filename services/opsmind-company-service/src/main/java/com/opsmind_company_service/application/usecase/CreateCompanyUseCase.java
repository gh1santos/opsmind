package com.opsmind_company_service.application.usecase;

import com.opsmind_company_service.application.dto.request.CreateCompanyRequest;
import com.opsmind_company_service.application.dto.response.CompanyResponse;

public interface CreateCompanyUseCase {
    CompanyResponse execute(CreateCompanyRequest request);
}
