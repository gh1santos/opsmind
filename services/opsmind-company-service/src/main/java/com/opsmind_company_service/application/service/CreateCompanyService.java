package com.opsmind_company_service.application.service;

import com.opsmind.events.company.CompanyCreatedEvent;
import com.opsmind_company_service.application.dto.request.CreateCompanyRequest;
import com.opsmind_company_service.application.dto.response.CompanyResponse;
import com.opsmind_company_service.application.exception.BusinessException;
import com.opsmind_company_service.application.port.CompanyEventPort;
import com.opsmind_company_service.application.usecase.CreateCompanyUseCase;
import com.opsmind_company_service.domain.entity.Company;
import com.opsmind_company_service.domain.entity.Membership;
import com.opsmind_company_service.domain.enums.CompanyStatus;
import com.opsmind_company_service.domain.enums.MemberRole;
import com.opsmind_company_service.domain.enums.PlanType;
import com.opsmind_company_service.domain.repository.CompanyRepository;
import com.opsmind_company_service.domain.repository.MembershipRepository;
import com.opsmind_company_service.domain.repository.SubscriptionPlanRepository;
import com.opsmind_company_service.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCompanyService implements CreateCompanyUseCase {

    private final CompanyRepository          companyRepository;
    private final MembershipRepository       membershipRepository;
    private final SubscriptionPlanRepository planRepository;
    private final CompanyEventPort           companyEventPort;

    @Override
    @Transactional
    public CompanyResponse execute(CreateCompanyRequest request) {

        String ownerEmail = UserContext.getEmail();
        UUID tenantId     = UserContext.getTenantId();

        if (tenantId == null) {
            throw new BusinessException("Tenant context not found");
        }

        // Uma empresa por tenant (na criação inicial)
        if (companyRepository.existsByTenantId(tenantId)) {
            throw new BusinessException("A company already exists for this tenant");
        }

        // Slug gerado a partir do nome
        String slug = generateSlug(request.getName());
        if (companyRepository.existsBySlug(slug)) {
            slug = slug + "-" + tenantId.toString().substring(0, 8);
        }

        // Plano FREE como padrão
        var freePlan = planRepository.findByType(PlanType.FREE);
        Long planId = freePlan.map(p -> p.getId()).orElse(null);
        String planType = freePlan.map(p -> p.getType().name()).orElse(PlanType.FREE.name());

        Company company = Company.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .slug(slug)
                .document(request.getDocument())
                .industry(request.getIndustry())
                .status(CompanyStatus.TRIAL)
                .ownerEmail(ownerEmail)
                .planId(planId)
                .trialEndsAt(LocalDateTime.now().plusDays(14))
                .createdAt(LocalDateTime.now())
                .build();

        Company saved = companyRepository.save(company);

        // Criador vira OWNER automaticamente
        Membership ownerMembership = Membership.builder()
                .companyId(saved.getId())
                .tenantId(tenantId)
                .userEmail(ownerEmail)
                .role(MemberRole.OWNER)
                .active(true)
                .joinedAt(LocalDateTime.now())
                .build();

        membershipRepository.save(ownerMembership);

        // Publica evento de criação de empresa
        companyEventPort.publish(CompanyCreatedEvent.of(
                saved.getId(),
                tenantId,
                saved.getName(),
                saved.getSlug(),
                ownerEmail,
                planType
        ));

        return toResponse(saved);
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    private CompanyResponse toResponse(Company c) {
        return CompanyResponse.builder()
                .id(c.getId()).tenantId(c.getTenantId()).name(c.getName())
                .slug(c.getSlug()).document(c.getDocument()).industry(c.getIndustry())
                .status(c.getStatus()).ownerEmail(c.getOwnerEmail())
                .trialEndsAt(c.getTrialEndsAt()).createdAt(c.getCreatedAt()).build();
    }
}
