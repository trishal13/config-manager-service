package com.projects.config_manager_service.controller;

import com.projects.config_manager_service.common.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HealthCheckControllerTest {

    private final HealthCheckController controller = new HealthCheckController();

    @Test
    void health_shouldReturnOkWithHealthyPayload() {
        ResponseEntity<ApiResponse<Map<String, String>>> response = controller.health();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Service is healthy", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().size());
        assertEquals("UP", response.getBody().getData().get(0).get("status"));
    }
}