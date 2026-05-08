package com.opsmind_auth_service.presentation.controller;

import com.opsmind_auth_service.application.dto.LoginRequest;
import com.opsmind_auth_service.application.dto.LoginResponse;
import com.opsmind_auth_service.application.dto.RegisterUserRequest;
import com.opsmind_auth_service.application.dto.RegisterUserResponse;
import com.opsmind_auth_service.application.usecase.LoginUseCase;
import com.opsmind_auth_service.application.usecase.RegisterUserUseCase;
import com.opsmind_auth_service.presentation.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.opsmind_auth_service.application.dto.RefreshTokenRequest;
import com.opsmind_auth_service.application.usecase.RefreshTokenUseCase;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RegisterUserResponse> register(
            @Valid @RequestBody RegisterUserRequest request
    ) {

        RegisterUserResponse response =
                registerUserUseCase.execute(request);

        return ApiResponse.<RegisterUserResponse>builder()
                .success(true)
                .data(response)
                .message("User registered successfully")
                .build();
    }

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response =
                loginUseCase.execute(request);

        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .data(response)
                .message("Login successful")
                .build();
    }

    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        LoginResponse response =
                refreshTokenUseCase.execute(request);

        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .data(response)
                .message("Token refreshed successfully")
                .build();
    }
}
