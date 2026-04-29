package com.projects.config_manager_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_ERROR("ERR_9000", "Invalid Request", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("ERR_9001", "Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
