package com.projects.config_manager_service.mapper;

import com.projects.config_manager_service.dto.ConfigResponseDTO;
import com.projects.config_manager_service.entity.ConfigManager;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConfigManagerMapper {

    public ConfigResponseDTO toConfigResponseDTO(ConfigManager configManager){
        if (ObjectUtils.isEmpty(configManager)){
            return null;
        }

        return ConfigResponseDTO.builder()
                .configIdentifier(configManager.getConfigIdentifier())
                .configType(configManager.getConfigType())
                .configData(configManager.getConfigData())
                .createdAt(configManager.getCreatedAt())
                .createdBy(configManager.getCreatedBy())
                .updatedAt(configManager.getUpdatedAt())
                .updatedBy(configManager.getUpdatedBy())
                .build();
    }

    public ConfigResponseDTO toUpdateConfigResponseDTO(ConfigManager config, Map<String, Object> updatedKeys) {
        if (ObjectUtils.isEmpty(config)) {
            return null;
        }

        return ConfigResponseDTO.builder()
                .configIdentifier(config.getConfigIdentifier())
                .configType(config.getConfigType())
                .configData(updatedKeys)
                .createdAt(config.getCreatedAt())
                .createdBy(config.getCreatedBy())
                .updatedAt(config.getUpdatedAt())
                .updatedBy(config.getUpdatedBy())
                .build();
    }
}
