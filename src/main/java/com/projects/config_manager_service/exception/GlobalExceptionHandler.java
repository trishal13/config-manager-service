package com.projects.config_manager_service.exception;

import com.projects.config_manager_service.common.ApiResponse;
import com.projects.config_manager_service.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponse<Object>> handleServiceException(ServiceException ex) {
        log.warn("[GlobalExceptionHandler.handleServiceException] Service exception occurred: {}", ex.getMessage());

        ErrorCode code = ex.getErrorCode();

        ApiResponse.ErrorDetail error =
                ApiResponse.ErrorDetail.builder()
                        .errorCode(code.getCode())
                        .errorMessage(ObjectUtils.isEmpty(ex.getMessage()) ? code.getMessage() : ex.getMessage())
                        .build();

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .errors(List.of(error))
                .build();

        return ResponseEntity
                .status(code.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("[GlobalExceptionHandler.handleGenericException] Unhandled exception", ex);

        ErrorCode code = ErrorCode.INTERNAL_ERROR;

        ApiResponse.ErrorDetail error =
                ApiResponse.ErrorDetail.builder()
                        .errorCode(code.getCode())
                        .errorMessage(code.getMessage())
                        .build();

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .errors(List.of(error))
                .build();

        return ResponseEntity
                .status(code.getStatus())
                .body(response);
    }
}