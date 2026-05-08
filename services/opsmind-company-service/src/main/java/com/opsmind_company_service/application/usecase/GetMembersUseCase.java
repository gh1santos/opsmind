package com.opsmind_company_service.application.usecase;

import com.opsmind_company_service.application.dto.response.MembershipResponse;

import java.util.List;

public interface GetMembersUseCase {
    List<MembershipResponse> execute(Long companyId);
}
