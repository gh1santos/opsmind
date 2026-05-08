package com.opsmind_auth_service.presentation.handler;

import com.opsmind_auth_service.application.exception.BusinessException;
import com.opsmind_auth_service.presentation.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBusinessException(
            BusinessException ex
    ) {

        return ApiResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        String message = ex
                .getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return ApiResponse.<Void>builder()
                .success(false)
                .message(message)
                .build();
    }
}
