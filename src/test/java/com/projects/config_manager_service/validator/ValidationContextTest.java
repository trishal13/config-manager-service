package com.projects.config_manager_service.validator;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationContextTest {

    @Test
    void putAndGet_shouldReturnStoredValue() {
        ValidationContext context = ValidationContext.create();
        context.put(String.class, "value");

        String value = context.get(String.class);

        assertEquals("value", value);
    }

    @Test
    void contains_shouldReturnTrueWhenTypeExists() {
        ValidationContext context = ValidationContext.create();
        context.put(Integer.class, 10);

        assertTrue(context.contains(Integer.class));
    }

    @Test
    void contains_shouldReturnFalseWhenTypeMissing() {
        ValidationContext context = ValidationContext.create();

        assertFalse(context.contains(Long.class));
    }

    @Test
    void get_whenMissing_shouldThrowIllegalArgumentException() {
        ValidationContext context = ValidationContext.create();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> context.get(String.class));

        assertEquals("No value found in ValidationContext for type: String", ex.getMessage());
    }

    @Test
    void get_whenEmptyString_shouldThrowIllegalArgumentException() {
        ValidationContext context = ValidationContext.create();
        context.put(String.class, "");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> context.get(String.class));

        assertEquals("No value found in ValidationContext for type: String", ex.getMessage());
    }

    @Test
    void get_whenEmptyCollection_shouldThrowIllegalArgumentException() {
        ValidationContext context = ValidationContext.create();
        context.put(List.class, List.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> context.get(List.class));

        assertEquals("No value found in ValidationContext for type: List", ex.getMessage());
    }
}
