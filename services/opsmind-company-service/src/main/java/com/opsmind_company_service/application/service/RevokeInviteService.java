package com.opsmind_company_service.application.service;

import com.opsmind_company_service.application.exception.BusinessException;
import com.opsmind_company_service.application.exception.ForbiddenException;
import com.opsmind_company_service.application.usecase.RevokeInviteUseCase;
import com.opsmind_company_service.domain.entity.CompanyInvite;
import com.opsmind_company_service.domain.entity.Membership;
import com.opsmind_company_service.domain.enums.InviteStatus;
import com.opsmind_company_service.domain.enums.MemberRole;
import com.opsmind_company_service.domain.repository.CompanyInviteRepository;
import com.opsmind_company_service.domain.repository.MembershipRepository;
import com.opsmind_company_service.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RevokeInviteService implements RevokeInviteUseCase {

    private final CompanyInviteRepository inviteRepository;
    private final MembershipRepository membershipRepository;

    @Override
    @Transactional
    public void execute(Long companyId, Long inviteId) {

        String userEmail = UserContext.getEmail();

        Membership requester = membershipRepository
                .findByCompanyIdAndUserEmail(companyId, userEmail)
                .orElseThrow(() -> new ForbiddenException("You are not a member of this company"));

        if (requester.getRole() == MemberRole.MEMBER ||
                requester.getRole() == MemberRole.VIEWER) {
            throw new ForbiddenException("Only OWNER or ADMIN can revoke invites");
        }

        CompanyInvite invite = inviteRepository.findByToken(inviteId.toString())
                .orElseThrow(() -> new BusinessException("Invite not found"));

        if (invite.getStatus() != InviteStatus.PENDING) {
            throw new BusinessException("Only PENDING invites can be revoked");
        }

        inviteRepository.updateStatus(invite.getId(), InviteStatus.REVOKED);
    }
}
