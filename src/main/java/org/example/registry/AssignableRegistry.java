package org.example.registry;

import java.util.Map;
import java.util.Optional;

public class AssignableRegistry implements Registry {

    private final Map<Class<?>, Instance> registry;
    private final InstanceFactory instanceFactory;

    public AssignableRegistry(final Map<Class<?>, Instance> registry,
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
        return getWrappedInstance(target)
            .map(Instance::instance);
    }

    @Override
    public boolean isProcessing(final Class<?> target) {
        return getWrappedInstance(target)
            .map(Instance::state).stream()
            .anyMatch(State.PROCESSING::equals);
    }

    private Optional<Instance> getWrappedInstance(final Class<?> target) {
        return registry.keySet().stream()
            .filter(target::isAssignableFrom)
            .findAny()
            .map(registry::get);
    }
}
