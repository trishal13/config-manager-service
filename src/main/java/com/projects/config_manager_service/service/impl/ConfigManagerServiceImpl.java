package com.projects.config_manager_service.service.impl;

import com.projects.config_manager_service.dto.ConfigUpdateRequestDTO;
import com.projects.config_manager_service.entity.ConfigManager;
import com.projects.config_manager_service.enums.ConfigType;
import com.projects.config_manager_service.enums.ErrorCode;
import com.projects.config_manager_service.exception.ServiceException;
import com.projects.config_manager_service.repository.ConfigManagerRepository;
import com.projects.config_manager_service.service.ConfigManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.projects.config_manager_service.common.Constants.USER_UUID_MDC_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigManagerServiceImpl implements ConfigManagerService {

    private final ConfigManagerRepository configManagerRepository;

    @Override
    public ConfigManager fetchConfig(String configIdentifier, ConfigType configType) {
        log.info("[ConfigManagerServiceImpl.fetchConfig] configIdentifier={} and configType={}",
                configIdentifier, configType);

        return configManagerRepository
                .findByConfigIdentifierAndConfigType(configIdentifier, configType)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.CONFIG_NOT_FOUND,
                        "Config not found for identifier=" + configIdentifier + " and type=" + configType
                ));
    }

    @Override
    public ConfigManager updateConfig(String configIdentifier, ConfigType configType, ConfigUpdateRequestDTO configUpdateRequest) {
        log.info("[ConfigManagerServiceImpl.updateConfig] configIdentifier={} and configType={}",
                configIdentifier, configType);

        String userUuid = MDC.get(USER_UUID_MDC_KEY);

        ConfigManager updatedConfig = configManagerRepository.upsertConfig(
                configIdentifier, configType, configUpdateRequest.getConfigData(), userUuid
        );

        if (ObjectUtils.isEmpty(updatedConfig)){
            log.error("[ConfigManagerServiceImpl.updateConfig] upsert returned null for identifier={} type={}",
                    configIdentifier, configType);
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return updatedConfig;
    }
}