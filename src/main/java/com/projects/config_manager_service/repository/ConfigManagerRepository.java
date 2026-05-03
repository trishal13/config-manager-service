package com.projects.config_manager_service.repository;

import com.projects.config_manager_service.entity.ConfigManager;
import com.projects.config_manager_service.enums.ConfigType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConfigManagerRepository extends
        MongoRepository<ConfigManager, String>,
        ConfigManagerRepositoryCustom {

    Optional<ConfigManager> findByConfigIdentifierAndConfigType(String configIdentifier, ConfigType configType);
}
