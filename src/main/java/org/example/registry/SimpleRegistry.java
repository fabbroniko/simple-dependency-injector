package org.example.registry;

import java.util.Map;
import java.util.Optional;

public class SimpleRegistry implements Registry {

    private final Map<Class<?>, Object> registry;

    public SimpleRegistry(final Map<Class<?>, Object> registry) {
        this.registry = registry;
    }

    @Override
    public void insert(final Class<?> clazz, final Object instance) {
        registry.put(clazz, instance);
    }

    @Override
    public Optional<Object> getInstance(final Class<?> clazz) {
        return registry.keySet().stream()
            .filter(clazz::isAssignableFrom)
            .findAny()
            .map(registry::get);
    }
}
