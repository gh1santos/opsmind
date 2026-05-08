package com.opsmind_auth_service.application.usecase;

import com.opsmind_auth_service.application.dto.LogoutRequest;

public interface LogoutUseCase {

    void execute(LogoutRequest request);
}
