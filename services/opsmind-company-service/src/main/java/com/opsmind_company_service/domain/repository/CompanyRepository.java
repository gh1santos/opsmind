package com.opsmind_company_service.domain.repository;

import com.opsmind_company_service.domain.entity.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

    Company save(Company company);

    Optional<Company> findById(Long id);

    Optional<Company> findByTenantId(UUID tenantId);

    Optional<Company> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByTenantId(UUID tenantId);

    List<Company> findAllByOwnerEmail(String ownerEmail);
}
