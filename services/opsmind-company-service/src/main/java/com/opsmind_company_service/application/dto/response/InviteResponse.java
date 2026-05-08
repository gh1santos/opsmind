package com.opsmind_company_service.application.dto.response;

import com.opsmind_company_service.domain.enums.InviteStatus;
import com.opsmind_company_service.domain.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InviteResponse {

    private Long id;
    private String invitedEmail;
    private String invitedByEmail;
    private MemberRole role;
    private InviteStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
