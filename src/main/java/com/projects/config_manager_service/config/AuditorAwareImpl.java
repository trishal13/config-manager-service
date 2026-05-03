package com.projects.config_manager_service.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.projects.config_manager_service.common.Constants.USER_UUID_MDC_KEY;

@Slf4j
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String userUuid = MDC.get(USER_UUID_MDC_KEY);
        log.debug("[AuditorAwareImpl.getCurrentAuditor] Resolving auditor from MDC: {}", userUuid);
        return Optional.ofNullable(userUuid)
                .filter(StringUtils::hasText);
    }
}