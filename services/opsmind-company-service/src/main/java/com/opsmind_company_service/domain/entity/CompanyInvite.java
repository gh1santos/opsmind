package com.opsmind_company_service.domain.entity;

import com.opsmind_company_service.domain.enums.InviteStatus;
import com.opsmind_company_service.domain.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInvite {

    private Long id;

    private Long companyId;

    private UUID tenantId;

    /** Email de quem está sendo convidado */
    private String invitedEmail;

    /** Email de quem enviou o convite */
    private String invitedByEmail;

    /** Role que o convidado receberá ao aceitar */
    private MemberRole role;

    /** Token UUID enviado no link de convite */
    private String token;

    private InviteStatus status;

    private LocalDateTime expiresAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime createdAt;
}
