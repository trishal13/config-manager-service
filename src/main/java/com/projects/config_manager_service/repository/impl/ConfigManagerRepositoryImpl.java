package com.projects.config_manager_service.repository.impl;

import com.projects.config_manager_service.entity.ConfigManager;
import com.projects.config_manager_service.enums.ConfigType;
import com.projects.config_manager_service.repository.ConfigManagerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;

import static com.projects.config_manager_service.common.Constants.CONFIG_DATA;
import static com.projects.config_manager_service.common.Constants.CONFIG_IDENTIFIER;
import static com.projects.config_manager_service.common.Constants.CONFIG_TYPE;
import static com.projects.config_manager_service.common.Constants.CREATED_AT;
import static com.projects.config_manager_service.common.Constants.CREATED_BY;
import static com.projects.config_manager_service.common.Constants.UPDATED_AT;
import static com.projects.config_manager_service.common.Constants.UPDATED_BY;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ConfigManagerRepositoryImpl implements ConfigManagerRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public ConfigManager upsertConfig(String configIdentifier,
                                      ConfigType configType,
                                      Map<String, Object> configData,
                                      String userUuid) {
        log.debug("[ConfigManagerRepositoryImpl.upsertConfig] configIdentifier={}, configType={}",
                configIdentifier, configType);

        Instant now = Instant.now();

        Query query = buildQuery(configIdentifier, configType);
        Update update = buildUpdate(configIdentifier, configType, configData, userUuid, now);
        FindAndModifyOptions options = buildOptions();

        return mongoTemplate.findAndModify(query, update, options, ConfigManager.class);
    }

    private Query buildQuery(String configIdentifier, ConfigType configType){
        return Query.query(
                Criteria.where(CONFIG_IDENTIFIER).is(configIdentifier)
                        .and(CONFIG_TYPE).is(configType.name())
        );
    }

    private Update buildUpdate(String configIdentifier,
                               ConfigType configType,
                               Map<String, Object> configData,
                               String userUuid,
                               Instant now) {
        Update update = new Update();

        configData.forEach((key, value) ->
                update.set(CONFIG_DATA+"."+key, value));

        update.set(UPDATED_BY, userUuid);
        update.set(UPDATED_AT, now);

        update.setOnInsert(CONFIG_IDENTIFIER, configIdentifier);
        update.setOnInsert(CONFIG_TYPE, configType.name());
        update.setOnInsert(CREATED_BY, userUuid);
        update.setOnInsert(CREATED_AT, now);

        return update;
    }

    private FindAndModifyOptions buildOptions() {
        return FindAndModifyOptions.options()
                .upsert(true)      // create if not exists
                .returnNew(true);  // return the updated document
    }
}
