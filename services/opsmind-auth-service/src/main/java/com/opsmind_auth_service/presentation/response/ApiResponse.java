package com.opsmind_auth_service.presentation.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

    private boolean success;

    private T data;

    private String message;
}
