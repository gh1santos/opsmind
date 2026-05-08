package com.opsmind_company_service.application.usecase;

import com.opsmind_company_service.application.dto.request.InviteUserRequest;
import com.opsmind_company_service.application.dto.response.InviteResponse;

public interface InviteUserUseCase {
    InviteResponse execute(Long companyId, InviteUserRequest request);
}
