package com.opsmind_company_service.domain.repository;

import com.opsmind_company_service.domain.entity.CompanyInvite;
import com.opsmind_company_service.domain.enums.InviteStatus;

import java.util.List;
import java.util.Optional;

public interface CompanyInviteRepository {

    CompanyInvite save(CompanyInvite invite);

    Optional<CompanyInvite> findByToken(String token);

    Optional<CompanyInvite> findByCompanyIdAndInvitedEmail(Long companyId, String invitedEmail);

    List<CompanyInvite> findAllByCompanyIdAndStatus(Long companyId, InviteStatus status);

    void updateStatus(Long inviteId, InviteStatus status);
}
