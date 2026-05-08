package com.opsmind_company_service.infrastructure.persistence.repository;

import com.opsmind_company_service.infrastructure.persistence.entity.MembershipJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaMembershipRepository
        extends JpaRepository<MembershipJpaEntity, Long> {

    Optional<MembershipJpaEntity> findByCompanyIdAndUserEmail(Long companyId, String userEmail);

    List<MembershipJpaEntity> findAllByCompanyIdAndActiveTrue(Long companyId);

    List<MembershipJpaEntity> findAllByUserEmailAndActiveTrue(String userEmail);

    @Query("SELECT COUNT(m) FROM MembershipJpaEntity m WHERE m.companyId = :companyId AND m.active = true")
    int countActiveByCompanyId(@Param("companyId") Long companyId);

    @Modifying
    @Query("UPDATE MembershipJpaEntity m SET m.active = false WHERE m.id = :id")
    void deactivateById(@Param("id") Long id);
}
