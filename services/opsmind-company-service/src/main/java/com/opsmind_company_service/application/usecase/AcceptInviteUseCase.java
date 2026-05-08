package com.opsmind_company_service.application.usecase;

import com.opsmind_company_service.application.dto.response.MembershipResponse;

public interface AcceptInviteUseCase {
    MembershipResponse execute(String token, String acceptingUserEmail);
}
