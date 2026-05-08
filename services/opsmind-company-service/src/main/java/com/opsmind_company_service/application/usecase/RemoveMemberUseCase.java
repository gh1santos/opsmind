package com.opsmind_company_service.application.usecase;

public interface RemoveMemberUseCase {
    void execute(Long companyId, String memberEmail);
}
