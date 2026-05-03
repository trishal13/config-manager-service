package com.projects.config_manager_service.dto;

import com.projects.config_manager_service.enums.ConfigType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigResponseDTO {

    private String configIdentifier;
    private ConfigType configType;
    private Map<String, Object> configData;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
}
