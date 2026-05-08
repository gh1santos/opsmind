package com.opsmind_company_service.infrastructure.persistence.repository;

import com.opsmind_company_service.infrastructure.persistence.entity.CompanyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaCompanyRepository
        extends JpaRepository<CompanyJpaEntity, Long> {

    Optional<CompanyJpaEntity> findByTenantId(UUID tenantId);

    Optional<CompanyJpaEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByTenantId(UUID tenantId);

    List<CompanyJpaEntity> findAllByOwnerEmail(String ownerEmail);
}
