package com.projects.config_manager_service.repository;

import com.projects.config_manager_service.entity.ConfigManager;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConfigManagerRepository extends
        MongoRepository<ConfigManager, String>,
        ConfigManagerRepositoryCustom {

    Optional<ConfigManager> findByConfigIdentifierAndConfigType(String configIdentifier, String configType);
}
