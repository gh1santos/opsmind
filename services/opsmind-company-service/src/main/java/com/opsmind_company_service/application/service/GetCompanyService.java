package com.opsmind_company_service.application.service;

import com.opsmind_company_service.application.dto.response.CompanyResponse;
import com.opsmind_company_service.application.exception.BusinessException;
import com.opsmind_company_service.application.usecase.GetCompanyUseCase;
import com.opsmind_company_service.domain.entity.Company;
import com.opsmind_company_service.domain.repository.CompanyRepository;
import com.opsmind_company_service.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCompanyService implements GetCompanyUseCase {

    private final CompanyRepository companyRepository;

    @Override
    public CompanyResponse getById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Company not found"));
        assertTenantAccess(company.getTenantId());
        return toResponse(company);
    }

    @Override
    public CompanyResponse getByTenantId(UUID tenantId) {
        assertTenantAccess(tenantId);
        return companyRepository.findByTenantId(tenantId)
                .map(this::toResponse)
                .orElseThrow(() -> new BusinessException("Company not found for this tenant"));
    }

    @Override
    public CompanyResponse getMyCompany() {
        UUID tenantId = UserContext.getTenantId();
        if (tenantId == null) throw new BusinessException("Tenant context not found");
        return companyRepository.findByTenantId(tenantId)
                .map(this::toResponse)
                .orElseThrow(() -> new BusinessException("No company found for your account"));
    }

    /** Garante que o usuário corrente tem acesso ao tenant solicitado */
    private void assertTenantAccess(UUID companyTenantId) {
        UUID currentTenant = UserContext.getTenantId();
        if (currentTenant != null && !currentTenant.equals(companyTenantId)) {
            throw new BusinessException("Access denied to this company");
        }
    }

    private CompanyResponse toResponse(Company c) {
        return CompanyResponse.builder()
                .id(c.getId()).tenantId(c.getTenantId()).name(c.getName())
                .slug(c.getSlug()).document(c.getDocument()).industry(c.getIndustry())
                .status(c.getStatus()).ownerEmail(c.getOwnerEmail())
                .trialEndsAt(c.getTrialEndsAt()).createdAt(c.getCreatedAt()).build();
    }
}
