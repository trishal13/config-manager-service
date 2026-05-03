package com.projects.config_manager_service.dto;

import com.projects.config_manager_service.enums.ConfigType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigFetchRequestDTO {

    private String configIdentifier;
    private ConfigType configType;
}