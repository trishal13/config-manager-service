package com.projects.config_manager_service.controller;

import com.projects.config_manager_service.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> health() {
        log.info("[HealthCheckController.health] Running health check controller!");
        ApiResponse<List<Map<String, String>>> response = ApiResponse.<List<Map<String, String>>>builder()
                .success(true)
                .message("Service is healthy")
                .data(List.of(Map.of("status", "UP")))
                .build();
        return ResponseEntity.ok(response);
    }
}
