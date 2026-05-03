package com.projects.config_manager_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Document("config_manager")
@CompoundIndex(
        name = "config_manager_index",
        def = "{'configIdentifier': 1, 'configType': 1}",
        unique = true
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigManager {

    @Id
    private String id;

    @Field("config_identifier")
    private String configIdentifier;

    @Field("config_type")
    private String configType;

    @Field("config_data")
    private Map<String, Object> configData;

    @CreatedBy
    @Field("created_by")
    private String createdBy;

    @Field("created_at")
    @CreatedDate
    private Instant createdAt;

    @LastModifiedBy
    @Field("updated_by")
    private String updatedBy;

    @Field("updated_at")
    @LastModifiedDate
    private Instant updatedAt;
}
