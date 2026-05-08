package com.opsmind_company_service.application.usecase;

public interface RevokeInviteUseCase {
    void execute(Long companyId, Long inviteId);
}
