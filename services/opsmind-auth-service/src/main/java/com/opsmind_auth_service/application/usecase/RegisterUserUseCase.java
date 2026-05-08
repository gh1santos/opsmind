package com.opsmind_auth_service.application.usecase;

import com.opsmind_auth_service.application.dto.RegisterUserRequest;
import com.opsmind_auth_service.application.dto.RegisterUserResponse;

public interface RegisterUserUseCase {

    RegisterUserResponse execute(RegisterUserRequest request);
}
