    package com.projects.config_manager_service.controller;

    import com.projects.config_manager_service.common.ApiResponse;
    import com.projects.config_manager_service.dto.ConfigResponseDTO;
    import com.projects.config_manager_service.dto.ConfigUpdateRequestDTO;
    import com.projects.config_manager_service.entity.ConfigManager;
    import com.projects.config_manager_service.enums.ConfigType;
    import com.projects.config_manager_service.mapper.ConfigManagerMapper;
    import com.projects.config_manager_service.service.ConfigManagerService;
    import jakarta.validation.Valid;
    import jakarta.validation.constraints.NotBlank;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @Slf4j
    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/configs")
    public class ConfigManagerController {

        private final ConfigManagerService configManagerService;
        private final ConfigManagerMapper configManagerMapper;

        @GetMapping("/{configIdentifier}/{configType}")
        public ResponseEntity<ApiResponse<ConfigResponseDTO>> fetchConfig(
                @PathVariable @NotBlank(message = "configIdentifier must not be blank") String configIdentifier,
                @PathVariable ConfigType configType
        ) {
            log.info("[ConfigManagerController.fetchConfig] configIdentifier={} and configType={}",
                    configIdentifier, configType);
            ConfigManager config = configManagerService.fetchConfig(configIdentifier, configType);

            ConfigResponseDTO responseData = configManagerMapper.toConfigResponseDTO(config);

            ApiResponse<ConfigResponseDTO> response = ApiResponse.success(
                    responseData, "Config Fetched Successfully");

            return ResponseEntity.ok(response);
        }

        @PutMapping("/{configIdentifier}/{configType}")
        public ResponseEntity<ApiResponse<ConfigResponseDTO>> updateConfig(
                @PathVariable @NotBlank(message = "configIdentifier must not be blank") String configIdentifier,
                @PathVariable ConfigType configType,
                @RequestBody @Valid ConfigUpdateRequestDTO configUpdateRequest
                ) {
            log.info("[ConfigManagerController.updateConfig] configIdentifier={}, configType={}, payload={}",
                    configIdentifier, configType, configUpdateRequest);

            ConfigManager config = configManagerService.updateConfig(configIdentifier, configType, configUpdateRequest);

            ConfigResponseDTO responseData = configManagerMapper.toUpdateConfigResponseDTO(config, configUpdateRequest.getConfigData());

            ApiResponse<ConfigResponseDTO> response = ApiResponse.success(responseData, "Config Updated Successfully");

            return ResponseEntity.ok(response);

        }
    }
