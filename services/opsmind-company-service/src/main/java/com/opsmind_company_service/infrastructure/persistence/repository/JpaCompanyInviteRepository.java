package com.opsmind_company_service.infrastructure.persistence.repository;

import com.opsmind_company_service.domain.enums.InviteStatus;
import com.opsmind_company_service.infrastructure.persistence.entity.CompanyInviteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaCompanyInviteRepository
        extends JpaRepository<CompanyInviteJpaEntity, Long> {

    Optional<CompanyInviteJpaEntity> findByToken(String token);

    Optional<CompanyInviteJpaEntity> findByCompanyIdAndInvitedEmail(Long companyId, String invitedEmail);

    List<CompanyInviteJpaEntity> findAllByCompanyIdAndStatus(Long companyId, InviteStatus status);

    @Modifying
    @Query("UPDATE CompanyInviteJpaEntity i SET i.status = :status WHERE i.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") InviteStatus status);
}
