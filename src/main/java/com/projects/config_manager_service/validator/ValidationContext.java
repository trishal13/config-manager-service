package com.projects.config_manager_service.validator;

import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationContext {

    private final Map<Class<?>, Object> store = new HashMap<>();

    // Store any object — key is its class type
    public <T> ValidationContext put(Class<T> type, T value) {
        store.put(type, value);
        return this; // fluent chaining
    }

    // Retrieve — fully typed, no casting needed by caller
    public <T> T get(Class<T> type) {
        Object value = store.get(type);
        if (ObjectUtils.isEmpty(value)) {
            throw new IllegalArgumentException(
                    "No value found in ValidationContext for type: " + type.getSimpleName()
            );
        }
        return type.cast(value);
    }

    // Check before getting — use in validators that have optional data
    public <T> boolean contains(Class<T> type) {
        return store.containsKey(type);
    }
}