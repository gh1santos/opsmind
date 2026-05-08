package com.opsmind_company_service.application.service;

import com.opsmind.events.company.InviteAcceptedEvent;
import com.opsmind_company_service.application.dto.response.MembershipResponse;
import com.opsmind_company_service.application.exception.BusinessException;
import com.opsmind_company_service.application.port.CompanyEventPort;
import com.opsmind_company_service.application.usecase.AcceptInviteUseCase;
import com.opsmind_company_service.domain.entity.CompanyInvite;
import com.opsmind_company_service.domain.entity.Membership;
import com.opsmind_company_service.domain.enums.InviteStatus;
import com.opsmind_company_service.domain.repository.CompanyInviteRepository;
import com.opsmind_company_service.domain.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AcceptInviteService implements AcceptInviteUseCase {

    private final CompanyInviteRepository inviteRepository;
    private final MembershipRepository    membershipRepository;
    private final CompanyEventPort        companyEventPort;

    @Override
    @Transactional
    public MembershipResponse execute(String token, String acceptingUserEmail) {

        CompanyInvite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("Invalid or not found invite token"));

        if (invite.getStatus() != InviteStatus.PENDING) {
            throw new BusinessException("Invite is no longer valid (status: " + invite.getStatus() + ")");
        }

        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            inviteRepository.updateStatus(invite.getId(), InviteStatus.EXPIRED);
            throw new BusinessException("Invite has expired");
        }

        // O email que aceita deve bater com o convidado
        if (!invite.getInvitedEmail().equalsIgnoreCase(acceptingUserEmail)) {
            throw new BusinessException("This invite was sent to a different email address");
        }

        // Verifica se já é membro ativo
        membershipRepository.findByCompanyIdAndUserEmail(invite.getCompanyId(), acceptingUserEmail)
                .filter(m -> m.getActive())
                .ifPresent(m -> {
                    throw new BusinessException("You are already a member of this company");
                });

        // Cria membership
        Membership membership = Membership.builder()
                .companyId(invite.getCompanyId())
                .tenantId(invite.getTenantId())
                .userEmail(acceptingUserEmail)
                .role(invite.getRole())
                .active(true)
                .joinedAt(LocalDateTime.now())
                .build();

        Membership saved = membershipRepository.save(membership);

        // Marca convite como aceito
        inviteRepository.updateStatus(invite.getId(), InviteStatus.ACCEPTED);

        // Publica evento — billing-service contabiliza seat, notification-service envia boas-vindas
        companyEventPort.publish(InviteAcceptedEvent.of(
                invite.getCompanyId(),
                invite.getTenantId(),
                saved.getId(),
                saved.getUserEmail(),
                saved.getRole().name()
        ));

        return MembershipResponse.builder()
                .id(saved.getId()).userEmail(saved.getUserEmail())
                .role(saved.getRole()).joinedAt(saved.getJoinedAt()).build();
    }
}
