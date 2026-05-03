package com.projects.config_manager_service.service;

import com.projects.config_manager_service.dto.ConfigUpdateRequestDTO;
import com.projects.config_manager_service.entity.ConfigManager;
import com.projects.config_manager_service.enums.ConfigType;

public interface ConfigManagerService {

    ConfigManager fetchConfig(String configIdentifier, ConfigType configType);

    ConfigManager updateConfig(String configIdentifier, ConfigType configType, ConfigUpdateRequestDTO configUpdateRequest);
}