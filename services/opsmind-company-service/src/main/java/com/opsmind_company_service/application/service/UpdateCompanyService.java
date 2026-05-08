package com.opsmind_company_service.application.service;

import com.opsmind_company_service.application.dto.request.UpdateCompanyRequest;
import com.opsmind_company_service.application.dto.response.CompanyResponse;
import com.opsmind_company_service.application.exception.BusinessException;
import com.opsmind_company_service.application.exception.ForbiddenException;
import com.opsmind_company_service.application.usecase.UpdateCompanyUseCase;
import com.opsmind_company_service.domain.entity.Company;
import com.opsmind_company_service.domain.entity.Membership;
import com.opsmind_company_service.domain.enums.MemberRole;
import com.opsmind_company_service.domain.repository.CompanyRepository;
import com.opsmind_company_service.domain.repository.MembershipRepository;
import com.opsmind_company_service.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateCompanyService implements UpdateCompanyUseCase {

    private final CompanyRepository companyRepository;
    private final MembershipRepository membershipRepository;

    @Override
    @Transactional
    public CompanyResponse execute(Long companyId, UpdateCompanyRequest request) {

        String userEmail = UserContext.getEmail();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Company not found"));

        // Verifica se o usuário tem role OWNER ou ADMIN
        Membership membership = membershipRepository
                .findByCompanyIdAndUserEmail(companyId, userEmail)
                .orElseThrow(() -> new ForbiddenException("You are not a member of this company"));

        if (membership.getRole() == MemberRole.MEMBER ||
                membership.getRole() == MemberRole.VIEWER) {
            throw new ForbiddenException("Only OWNER or ADMIN can update company");
        }

        // Aplica apenas os campos enviados (PATCH semântico)
        Company updated = Company.builder()
                .id(company.getId())
                .tenantId(company.getTenantId())
                .name(request.getName() != null ? request.getName() : company.getName())
                .slug(company.getSlug())
                .document(request.getDocument() != null ? request.getDocument() : company.getDocument())
                .industry(request.getIndustry() != null ? request.getIndustry() : company.getIndustry())
                .status(company.getStatus())
                .ownerEmail(company.getOwnerEmail())
                .planId(company.getPlanId())
                .trialEndsAt(company.getTrialEndsAt())
                .createdAt(company.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Company saved = companyRepository.save(updated);

        return CompanyResponse.builder()
                .id(saved.getId()).tenantId(saved.getTenantId()).name(saved.getName())
                .slug(saved.getSlug()).document(saved.getDocument()).industry(saved.getIndustry())
                .status(saved.getStatus()).ownerEmail(saved.getOwnerEmail())
                .trialEndsAt(saved.getTrialEndsAt()).createdAt(saved.getCreatedAt()).build();
    }
}
