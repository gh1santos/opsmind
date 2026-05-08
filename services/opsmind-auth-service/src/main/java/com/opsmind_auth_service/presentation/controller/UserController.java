package com.opsmind_auth_service.presentation.controller;

import com.opsmind_auth_service.presentation.response.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public ApiResponse<String> me(
            Authentication authentication
    ) {

        return ApiResponse.<String>builder()
                .success(true)
                .data(authentication.getName())
                .message("Authenticated user")
                .build();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<String> admin() {

        return ApiResponse.<String>builder()
                .success(true)
                .data("Admin access granted")
                .message("Authorized")
                .build();
    }
}
