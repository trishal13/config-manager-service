package com.projects.config_manager_service.validator;

import com.projects.config_manager_service.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.projects.config_manager_service.enums.ErrorCode.VALIDATION_ERROR;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorExecutorTest {

    private final ValidatorExecutor executor = new ValidatorExecutor();
    private final ValidationContext context = new ValidationContext();

    @Test
    void executeFailFast_whenAllValidatorsPass_shouldNotThrow() {
        Validator pass1 = ctx -> { };
        Validator pass2 = ctx -> { };

        assertDoesNotThrow(() -> executor.executeFailFast(List.of(pass1, pass2), context));
    }

    @Test
    void executeFailFast_whenValidatorFails_shouldStopAtFirstFailure() {
        AtomicInteger counter = new AtomicInteger(0);
        Validator fail = ctx -> {
            counter.incrementAndGet();
            throw new ServiceException(VALIDATION_ERROR, "fail-fast");
        };
        Validator shouldNotRun = ctx -> counter.incrementAndGet();

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> executor.executeFailFast(List.of(fail, shouldNotRun), context)
        );

        assertEquals("fail-fast", ex.getMessage());
        assertEquals(1, counter.get());
    }

    @Test
    void executeAll_whenAllValidatorsPass_shouldNotThrow() {
        Validator pass1 = ctx -> { };
        Validator pass2 = ctx -> { };

        assertDoesNotThrow(() -> executor.executeAll(List.of(pass1, pass2), context));
    }

    @Test
    void executeAll_whenMultipleServiceExceptions_shouldAggregateMessages() {
        Validator fail1 = ctx -> { throw new ServiceException(VALIDATION_ERROR, "error-1"); };
        Validator fail2 = ctx -> { throw new ServiceException(VALIDATION_ERROR, "error-2"); };

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> executor.executeAll(List.of(fail1, fail2), context)
        );

        assertEquals(VALIDATION_ERROR, ex.getErrorCode());
        assertEquals("error-1, error-2", ex.getMessage());
    }

    @Test
    void executeAll_whenUnexpectedException_shouldWrapAsUnexpectedValidationError() {
        class ExplodingValidator implements Validator {
            @Override
            public void validate(ValidationContext context) {
                throw new IllegalStateException("unexpected");
            }
        }
        Validator fail = new ExplodingValidator();

        ServiceException ex = assertThrows(
                ServiceException.class,
                () -> executor.executeAll(List.of(fail), context)
        );

        assertEquals(VALIDATION_ERROR, ex.getErrorCode());
        assertEquals("Unexpected validation error in ExplodingValidator", ex.getMessage());
    }
}
