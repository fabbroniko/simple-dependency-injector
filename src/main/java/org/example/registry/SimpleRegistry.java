package org.example.registry;

import java.util.Map;
import java.util.Optional;

public class SimpleRegistry implements Registry {

    private final Map<Class<?>, Instance> registry;

    public SimpleRegistry(final Map<Class<?>, Instance> registry) {
        this.registry = registry;
    }

    @Override
    public void process(final Class<?> target) {
        registry.put(target, new BaseInstance(State.PROCESSING, null));
    }

    @Override
    public void insert(final Class<?> target, final Object instance) {
        registry.put(target, new BaseInstance(State.COMPLETE, instance));
    }

    @Override
    public Optional<Object> getInstance(final Class<?> clazz) {
        return registry.keySet().stream()
            .filter(clazz::isAssignableFrom)
            .findAny()
            .map(registry::get)
            .map(Instance::instance);
    }

    @Override
    public boolean isProcessing(final Class<?> target) {
        final Instance instance = registry.get(target);
        if (instance == null) {
            return false;
        }

        return instance.state().equals(State.PROCESSING);
    }
}
