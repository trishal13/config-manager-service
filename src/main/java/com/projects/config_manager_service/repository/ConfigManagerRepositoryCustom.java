package com.projects.config_manager_service.repository;

import com.projects.config_manager_service.entity.ConfigManager;
import com.projects.config_manager_service.enums.ConfigType;

import java.util.Map;

public interface ConfigManagerRepositoryCustom {

    ConfigManager upsertConfig(String configIdentifier,
                               ConfigType configType,
                               Map<String, Object> configData,
                               String userUuid);
}