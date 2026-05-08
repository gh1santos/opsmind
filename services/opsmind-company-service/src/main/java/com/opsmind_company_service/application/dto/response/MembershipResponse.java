package com.opsmind_company_service.application.dto.response;

import com.opsmind_company_service.domain.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MembershipResponse {

    private Long id;
    private String userEmail;
    private MemberRole role;
    private LocalDateTime joinedAt;
}
