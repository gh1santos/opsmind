package com.opsmind_auth_service.application.usecase;

import com.opsmind_auth_service.application.dto.LoginRequest;
import com.opsmind_auth_service.application.dto.LoginResponse;

public interface LoginUseCase {

    LoginResponse execute(LoginRequest request);
}