package com.fabbroniko.sdi.registry;

import java.util.Map;
import java.util.Optional;

public class SimpleRegistry implements Registry {

    private final Map<Class<?>, Instance> registry;
    private final InstanceFactory instanceFactory;

    public SimpleRegistry(final Map<Class<?>, Instance> registry,
                          final InstanceFactory instanceFactory) {

        this.registry = registry;
        this.instanceFactory = instanceFactory;
    }

    @Override
    public void process(final Class<?> target) {
        registry.put(target, instanceFactory.createProcessing());
    }

    @Override
    public void insert(final Class<?> target, final Object instance) {
        registry.put(target, instanceFactory.createCompleted(instance));
    }

    @Override
    public Optional<Object> getInstance(final Class<?> target) {
        return Optional.ofNullable(registry.get(target))
            .filter(instance -> State.COMPLETE.equals(instance.state()))
            .map(Instance::instance);
    }

    @Override
    public boolean isProcessing(final Class<?> target) {
        return Optional.ofNullable(registry.get(target))
            .map(Instance::state).stream()
            .anyMatch(State.PROCESSING::equals);
    }
}
