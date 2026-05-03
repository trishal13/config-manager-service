package com.projects.config_manager_service.exception;

import com.projects.config_manager_service.common.ApiResponse;
import com.projects.config_manager_service.enums.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponse<Object>> handleServiceException(ServiceException ex) {
        log.warn("[GlobalExceptionHandler.handleServiceException] Service exception occurred: {}", ex.getMessage());

        ErrorCode code = ex.getErrorCode();
        ApiResponse<Object> response = ApiResponse.failure(code, ex.getMessage());

        return ResponseEntity
                .status(code.getStatus())
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("[GlobalExceptionHandler.handleHttpMessageNotReadable] Malformed request body: {}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.failure(ErrorCode.INVALID_REQUEST,
                "Malformed request body: please ensure all fields have the correct type");

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("[GlobalExceptionHandler.handleMethodArgumentNotValid] Validation failed: {}", message);

        ApiResponse<Object> response = ApiResponse.failure(ErrorCode.INVALID_REQUEST, message);

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message;

        Class<?> requiredType = ex.getRequiredType();

        if (requiredType != null && requiredType.isEnum()) {
            String allowedValues = Arrays.stream(requiredType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            message = "Invalid value '" + ex.getValue() + "' for '" + ex.getName() + "'. Must be one of: " + allowedValues;
        } else {
            message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
        }

        log.warn("[GlobalExceptionHandler.handleMethodArgumentTypeMismatch] message={}", message);

        ApiResponse<Object> response = ApiResponse.failure(ErrorCode.INVALID_REQUEST, message);

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("[GlobalExceptionHandler.handleIllegalArgument] message={}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.failure(ErrorCode.INVALID_REQUEST, ex.getMessage());

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("[GlobalExceptionHandler.handleGenericException] Unhandled exception", ex);

        ApiResponse<Object> response = ApiResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(response);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingPathVariable(MissingPathVariableException ex) {
        String message = "Invalid or missing value for path variable '" + ex.getVariableName() + "'";

        log.warn("[GlobalExceptionHandler.handleMissingPathVariable] message={}", message);

        ApiResponse<Object> response = ApiResponse.failure(ErrorCode.INVALID_REQUEST, message);

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        log.warn("[GlobalExceptionHandler.handleConstraintViolation] message={}", message);

        ApiResponse<Object> response = ApiResponse.failure(ErrorCode.INVALID_REQUEST, message);

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        String message = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("[GlobalExceptionHandler.handleHandlerMethodValidation] message={}", message);

        ApiResponse<Object> response = ApiResponse.failure(ErrorCode.INVALID_REQUEST, message);

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(response);
    }
}