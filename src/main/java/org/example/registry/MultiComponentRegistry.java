package org.example.registry;

import org.example.naming.QualifyingNameResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MultiComponentRegistry implements Registry {

    private final Map<Class<?>, Map<String, Instance>> registry;
    private final QualifyingNameResolver nameResolver;

    public MultiComponentRegistry(final Map<Class<?>, Map<String, Instance>> registry,
                                  final QualifyingNameResolver nameResolver) {

        this.registry = registry;
        this.nameResolver = nameResolver;
    }

    @Override
    public void process(final Class<?> target) {
        final String qualifyingName = nameResolver.resolveFor(target);
        if (!registry.containsKey(target)) {
            registry.put(target, new HashMap<>());
        }

        registry.get(target).put(qualifyingName, new InstanceStore(State.PROCESSING, null));
    }

    @Override
    public void insert(final Class<?> target, final Object instance) {
        final String qualifyingName = nameResolver.resolveFor(target);
        if (!registry.containsKey(target)) {
            registry.put(target, new HashMap<>());
        }

        registry.get(target).put(qualifyingName, new InstanceStore(State.COMPLETE, instance));
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
        final String qualifyingName = nameResolver.resolveFor(target);

        return registry.keySet().stream()
            .filter(target::isAssignableFrom)
            .findAny()
            .map(registry::get)
            .map(a -> a.get(qualifyingName));
    }
}
