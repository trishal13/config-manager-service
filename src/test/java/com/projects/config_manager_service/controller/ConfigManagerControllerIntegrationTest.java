package com.projects.config_manager_service.controller;

import com.projects.config_manager_service.entity.ConfigManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ConfigManagerControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        ConfigManager config = new ConfigManager();
        config.setConfigIdentifier("testConfig");
        config.setConfigType("testType");
        config.setConfigData(Map.of("key", "value")); // was wrongly set as a raw String
        mongoTemplate.save(config);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(ConfigManager.class);
    }

    @Test
    void fetchConfig_shouldReturnConfig_whenExists() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trace-Id", "123e4567-e89b-12d3-a456-426614174000");
        headers.set("X-User-Uuid", "user:123e4567-e89b-12d3-a456-426614174000");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/configs/testConfig/testType",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"success\":true"));
        assertTrue(response.getBody().contains("\"message\":\"Config Fetched Successfully\""));
        assertTrue(response.getBody().contains("\"config_identifier\":\"testConfig\""));
        assertTrue(response.getBody().contains("\"config_type\":\"testType\""));
    }

    @Test
    void fetchConfig_shouldReturnError_whenConfigNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trace-Id", "123e4567-e89b-12d3-a456-426614174000");
        headers.set("X-User-Uuid", "user:123e4567-e89b-12d3-a456-426614174000");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/configs/nonExistent/testType",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"success\":false"));
        // failure() populates errors array, not top-level message
        assertTrue(response.getBody().contains("\"error_code\":\"ERR_9002\""));
        assertTrue(response.getBody().contains("Config not found"));
    }
}