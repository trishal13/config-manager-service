package com.projects.config_manager_service.validator;

import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationContext {

    private final Map<Class<?>, Object> store = new HashMap<>();

    private ValidationContext() {}

    public static ValidationContext create() {
        return new ValidationContext();
    }

    public <T> ValidationContext put(Class<T> type, T value) {
        store.put(type, value);
        return this;
    }

    public <T> T get(Class<T> type) {
        Object value = store.get(type);
        if (ObjectUtils.isEmpty(value)) {
            throw new IllegalArgumentException(
                    "No value found in ValidationContext for type: " + type.getSimpleName()
            );
        }
        return type.cast(value);
    }

    public <T> T getOptional(Class<T> type) {
        return type.cast(store.get(type));
    }

    public <T> boolean contains(Class<T> type) {
        return store.containsKey(type);
    }
}