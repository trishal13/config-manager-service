package com.projects.config_manager_service.validator;

import com.projects.config_manager_service.enums.ErrorCode;
import com.projects.config_manager_service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ValidatorExecutor {

    // Stops at first failure
    public void executeFailFast(List<Validator> validators, ValidationContext context) {
        for (Validator validator : validators) {
            log.debug("[ValidatorExecutor.executeFailFast] Running validator: {}", validator.getClass().getSimpleName());
            validator.validate(context); // throws ServiceException on failure
        }
    }

    // Runs all, collects all errors
    public void executeAll(List<Validator> validators, ValidationContext context) {
        List<String> errors = new ArrayList<>();

        for (Validator validator : validators) {
            log.debug("[ValidatorExecutor.executeAll] Running validator: {}", validator.getClass().getSimpleName());
            try {
                validator.validate(context);
            } catch (ServiceException ex) {
                errors.add(ex.getMessage());
            } catch (Exception ex) {
                log.error("[ValidatorExecutor.executeAll] Unexpected error in validator: {}", validator.getClass().getSimpleName(), ex);
                errors.add("Unexpected validation error in " + validator.getClass().getSimpleName());
            }
        }

        if (!errors.isEmpty()) {
            log.warn("[ValidatorExecutor.executeAll] Validation failed with errors: {}", errors);
            throw new ServiceException(ErrorCode.VALIDATION_ERROR, String.join(", ", errors));
        }
    }
}