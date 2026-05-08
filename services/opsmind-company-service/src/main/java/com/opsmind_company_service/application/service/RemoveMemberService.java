package com.opsmind_company_service.application.service;

import com.opsmind.events.company.MemberRemovedEvent;
import com.opsmind_company_service.application.exception.BusinessException;
import com.opsmind_company_service.application.exception.ForbiddenException;
import com.opsmind_company_service.application.port.CompanyEventPort;
import com.opsmind_company_service.application.usecase.RemoveMemberUseCase;
import com.opsmind_company_service.domain.entity.Membership;
import com.opsmind_company_service.domain.enums.MemberRole;
import com.opsmind_company_service.domain.repository.MembershipRepository;
import com.opsmind_company_service.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RemoveMemberService implements RemoveMemberUseCase {

    private final MembershipRepository membershipRepository;
    private final CompanyEventPort     companyEventPort;

    @Override
    @Transactional
    public void execute(Long companyId, String memberEmail) {

        String requesterEmail = UserContext.getEmail();

        // Verifica permissão do solicitante
        Membership requester = membershipRepository
                .findByCompanyIdAndUserEmail(companyId, requesterEmail)
                .orElseThrow(() -> new ForbiddenException("You are not a member of this company"));

        if (requester.getRole() == MemberRole.MEMBER ||
                requester.getRole() == MemberRole.VIEWER) {
            throw new ForbiddenException("Only OWNER or ADMIN can remove members");
        }

        Membership target = membershipRepository
                .findByCompanyIdAndUserEmail(companyId, memberEmail)
                .orElseThrow(() -> new BusinessException("Member not found"));

        // OWNER não pode ser removido
        if (target.getRole() == MemberRole.OWNER) {
            throw new BusinessException("Cannot remove the company OWNER");
        }

        // ADMIN não pode remover outro ADMIN (apenas OWNER pode)
        if (target.getRole() == MemberRole.ADMIN &&
                requester.getRole() != MemberRole.OWNER) {
            throw new ForbiddenException("Only OWNER can remove ADMINs");
        }

        membershipRepository.deactivate(target.getId());

        // Publica evento — billing-service libera seat, audit-service registra
        companyEventPort.publish(MemberRemovedEvent.of(
                companyId,
                requester.getTenantId(),
                memberEmail,
                requesterEmail
        ));
    }
}
