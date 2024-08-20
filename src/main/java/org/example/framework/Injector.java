package org.example.framework;

import java.lang.reflect.Constructor;
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
        // Check if an instance is already registered
        if (instances.containsKey(interfaceType)) {
            return interfaceType.cast(instances.get(interfaceType));
        }

        try {
            Constructor<?> constructor = getConstructorWithMaxParams(interfaceType);

            // Resolve dependencies recursively
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = resolve(parameterTypes[i]);
            }

            // Create a new instance with the resolved dependencies
            //noinspection unchecked
            T instance = (T) constructor.newInstance(parameters);
            instances.put(interfaceType, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve dependency: " + interfaceType.getName(), e);
        }
    }

    private static <T> Constructor<?> getConstructorWithMaxParams(Class<T> interfaceType) {
        Constructor<?>[] constructors = interfaceType.getDeclaredConstructors();

        // Find the constructor with the most parameters
        Constructor<?> constructor = null;
        for (Constructor<?> c : constructors) {
            if (constructor == null || c.getParameterCount() > constructor.getParameterCount()) {
                constructor = c;
            }
        }

        if (constructor == null) {
            throw new RuntimeException("No suitable constructor found for: " + interfaceType.getName());
        }

        constructor.setAccessible(true); // Allow access to private constructors
        return constructor;
    }
}