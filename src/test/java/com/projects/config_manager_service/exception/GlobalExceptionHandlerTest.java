package com.projects.config_manager_service.exception;

import com.projects.config_manager_service.common.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.projects.config_manager_service.enums.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.projects.config_manager_service.enums.ErrorCode.INVALID_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleServiceException_whenMessageProvided_shouldUseCustomMessage() {
        ServiceException ex = new ServiceException(INVALID_REQUEST, "Field x is required");

        ResponseEntity<ApiResponse<Object>> response = handler.handleServiceException(ex);

        assertEquals(INVALID_REQUEST.getStatus(), response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals(INVALID_REQUEST.getCode(), response.getBody().getErrors().get(0).getErrorCode());
        assertEquals("Field x is required", response.getBody().getErrors().get(0).getErrorMessage());
    }

    @Test
    void handleServiceException_whenMessageNull_shouldUseDefaultErrorCodeMessage() {
        ServiceException ex = new ServiceException(INVALID_REQUEST, null);

        ResponseEntity<ApiResponse<Object>> response = handler.handleServiceException(ex);

        assertEquals(INVALID_REQUEST.getStatus(), response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals(INVALID_REQUEST.getMessage(), response.getBody().getErrors().get(0).getErrorMessage());
    }

    @Test
    void handleGenericException_shouldReturnInternalErrorPayload() {
        Exception ex = new RuntimeException("boom");

        ResponseEntity<ApiResponse<Object>> response = handler.handleGenericException(ex);

        assertEquals(INTERNAL_SERVER_ERROR.getStatus(), response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals(INTERNAL_SERVER_ERROR.getCode(), response.getBody().getErrors().get(0).getErrorCode());
        assertEquals(INTERNAL_SERVER_ERROR.getMessage(), response.getBody().getErrors().get(0).getErrorMessage());
    }
}