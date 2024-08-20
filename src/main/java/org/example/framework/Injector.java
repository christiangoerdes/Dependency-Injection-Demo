package org.example.framework;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Injector {

    private final Map<Class<?>, Object> instances = new HashMap<>();
    private final Map<Class<?>, Class<?>> interfaceImplementations = new HashMap<>();
    private final Set<Class<?>> currentlyResolving = new HashSet<>();

    // Registers a class with its instance
    public <T> void register(Class<T> interfaceType, T instance) {
        instances.put(interfaceType, instance);
    }

    public <T, U extends T> void registerImplementation(Class<T> abstractType, Class<U> concreteType) {
        interfaceImplementations.put(abstractType, concreteType);
    }

    // Resolves dependencies and returns an instance of the requested type
    public <T> T resolve(Class<T> interfaceType) {
        // Check if an instance is already registered
        if (instances.containsKey(interfaceType)) {
            return interfaceType.cast(instances.get(interfaceType));
        }

        // Check for a registered implementation
        Class<?> implementationType = interfaceImplementations.get(interfaceType);
        if (implementationType != null) {
            //noinspection unchecked
            return (T) resolve(implementationType);
        }

        // Check for circular dependencies
        if (currentlyResolving.contains(interfaceType)) {
            throw new RuntimeException("Circular dependency detected: " + interfaceType.getName());
        }

        try {
            // Mark this type as currently being resolved
            currentlyResolving.add(interfaceType);

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
        } finally {
            // Ensure we always remove the type from the currently resolving set
            currentlyResolving.remove(interfaceType);
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