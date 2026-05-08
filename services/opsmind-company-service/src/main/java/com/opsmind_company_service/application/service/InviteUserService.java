package com.opsmind_company_service.application.service;

import com.opsmind.events.company.InviteCreatedEvent;
import com.opsmind_company_service.application.dto.request.InviteUserRequest;
import com.opsmind_company_service.application.dto.response.InviteResponse;
import com.opsmind_company_service.application.exception.BusinessException;
import com.opsmind_company_service.application.exception.ForbiddenException;
import com.opsmind_company_service.application.port.CompanyEventPort;
import com.opsmind_company_service.application.usecase.InviteUserUseCase;
import com.opsmind_company_service.domain.entity.CompanyInvite;
import com.opsmind_company_service.domain.entity.Membership;
import com.opsmind_company_service.domain.enums.InviteStatus;
import com.opsmind_company_service.domain.enums.MemberRole;
import com.opsmind_company_service.domain.repository.CompanyInviteRepository;
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
public class InviteUserService implements InviteUserUseCase {

    private final CompanyRepository          companyRepository;
    private final MembershipRepository       membershipRepository;
    private final CompanyInviteRepository    inviteRepository;
    private final SubscriptionPlanRepository planRepository;
    private final CompanyEventPort           companyEventPort;

    @Override
    @Transactional
    public InviteResponse execute(Long companyId, InviteUserRequest request) {

        String inviterEmail = UserContext.getEmail();

        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException("Company not found"));

        // Verifica permissão do convidante
        Membership inviter = membershipRepository
                .findByCompanyIdAndUserEmail(companyId, inviterEmail)
                .orElseThrow(() -> new ForbiddenException("You are not a member of this company"));

        if (inviter.getRole() == MemberRole.MEMBER ||
                inviter.getRole() == MemberRole.VIEWER) {
            throw new ForbiddenException("Only OWNER or ADMIN can invite members");
        }

        // Não pode convidar para role OWNER
        if (request.getRole() == MemberRole.OWNER) {
            throw new BusinessException("Cannot invite as OWNER");
        }

        // Verifica se já é membro
        membershipRepository.findByCompanyIdAndUserEmail(companyId, request.getEmail())
                .filter(m -> m.getActive())
                .ifPresent(m -> {
                    throw new BusinessException("User is already a member of this company");
                });

        // Verifica limite de usuários do plano
        if (company.getPlanId() != null) {
            planRepository.findById(company.getPlanId()).ifPresent(plan -> {
                int currentMembers = membershipRepository.countActiveByCompanyId(companyId);
                if (plan.getMaxUsers() != -1 && currentMembers >= plan.getMaxUsers()) {
                    throw new BusinessException(
                            "User limit reached for your plan (" + plan.getMaxUsers() + "). Upgrade to invite more.");
                }
            });
        }

        // Cancela convite pendente anterior para o mesmo email
        inviteRepository.findByCompanyIdAndInvitedEmail(companyId, request.getEmail())
                .filter(i -> i.getStatus() == InviteStatus.PENDING)
                .ifPresent(i -> inviteRepository.updateStatus(i.getId(), InviteStatus.REVOKED));

        CompanyInvite invite = CompanyInvite.builder()
                .companyId(companyId)
                .tenantId(company.getTenantId())
                .invitedEmail(request.getEmail())
                .invitedByEmail(inviterEmail)
                .role(request.getRole())
                .token(UUID.randomUUID().toString())
                .status(InviteStatus.PENDING)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();

        CompanyInvite saved = inviteRepository.save(invite);

        // Publica InviteCreatedEvent — notification-service envia email com link de aceite
        companyEventPort.publish(InviteCreatedEvent.of(
                companyId,
                company.getName(),
                company.getTenantId(),
                saved.getId(),
                saved.getInvitedEmail(),
                saved.getInvitedByEmail(),
                saved.getRole().name(),
                saved.getToken(),
                saved.getExpiresAt().toInstant(java.time.ZoneOffset.UTC)
        ));

        return InviteResponse.builder()
                .id(saved.getId()).invitedEmail(saved.getInvitedEmail())
                .invitedByEmail(saved.getInvitedByEmail()).role(saved.getRole())
                .status(saved.getStatus()).expiresAt(saved.getExpiresAt())
                .createdAt(saved.getCreatedAt()).build();
    }
}
