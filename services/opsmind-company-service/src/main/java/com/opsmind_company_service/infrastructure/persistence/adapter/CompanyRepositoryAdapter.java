package com.opsmind_company_service.infrastructure.persistence.adapter;

import com.opsmind_company_service.domain.entity.Company;
import com.opsmind_company_service.domain.repository.CompanyRepository;
import com.opsmind_company_service.infrastructure.persistence.entity.CompanyJpaEntity;
import com.opsmind_company_service.infrastructure.persistence.repository.JpaCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final JpaCompanyRepository repository;

    @Override
    public Company save(Company company) {
        CompanyJpaEntity entity = toEntity(company);
        return toModel(repository.save(entity));
    }

    @Override
    public Optional<Company> findById(Long id) {
        return repository.findById(id).map(this::toModel);
    }

    @Override
    public Optional<Company> findByTenantId(UUID tenantId) {
        return repository.findByTenantId(tenantId).map(this::toModel);
    }

    @Override
    public Optional<Company> findBySlug(String slug) {
        return repository.findBySlug(slug).map(this::toModel);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return repository.existsBySlug(slug);
    }

    @Override
    public boolean existsByTenantId(UUID tenantId) {
        return repository.existsByTenantId(tenantId);
    }

    @Override
    public List<Company> findAllByOwnerEmail(String ownerEmail) {
        return repository.findAllByOwnerEmail(ownerEmail)
                .stream().map(this::toModel).collect(Collectors.toList());
    }

    private Company toModel(CompanyJpaEntity e) {
        return Company.builder()
                .id(e.getId()).tenantId(e.getTenantId()).name(e.getName())
                .slug(e.getSlug()).document(e.getDocument()).industry(e.getIndustry())
                .status(e.getStatus()).ownerEmail(e.getOwnerEmail()).planId(e.getPlanId())
                .trialEndsAt(e.getTrialEndsAt()).createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt()).build();
    }

    private CompanyJpaEntity toEntity(Company c) {
        CompanyJpaEntity e = new CompanyJpaEntity();
        e.setId(c.getId()); e.setTenantId(c.getTenantId()); e.setName(c.getName());
        e.setSlug(c.getSlug()); e.setDocument(c.getDocument()); e.setIndustry(c.getIndustry());
        e.setStatus(c.getStatus()); e.setOwnerEmail(c.getOwnerEmail()); e.setPlanId(c.getPlanId());
        e.setTrialEndsAt(c.getTrialEndsAt()); e.setCreatedAt(c.getCreatedAt());
        e.setUpdatedAt(c.getUpdatedAt());
        return e;
    }
}
