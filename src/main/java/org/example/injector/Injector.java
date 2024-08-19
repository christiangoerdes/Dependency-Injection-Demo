package org.example.injector;

import java.util.HashMap;
import java.util.Map;

public class Injector {

    private final Map<Class<?>, Object> instances = new HashMap<>();

    // Registers a class with its instance
    public <T> void register(Class<T> interfaceType, T instance) {
        instances.put(interfaceType, instance);
    }

    // Resolves dependencies and returns an instance of the requested type
    public <T> T resolve(Class<T> interfaceType) {
        return interfaceType.cast(instances.get(interfaceType));
    }
}