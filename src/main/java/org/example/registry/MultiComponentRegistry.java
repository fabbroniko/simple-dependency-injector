package org.example.registry;

import org.example.annotation.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MultiComponentRegistry implements Registry {

    private final Map<Class<?>, Map<String, Instance>> registry;

    public MultiComponentRegistry(final Map<Class<?>, Map<String, Instance>> registry) {

        this.registry = registry;
    }

    @Override
    public void process(final Class<?> target) {
        final String qualifyingName = qualifyingName(target);
        if (!registry.containsKey(target)) {
            registry.put(target, new HashMap<>());
        }

        registry.get(target).put(qualifyingName, new InstanceStore(State.PROCESSING, null));
    }

    @Override
    public void insert(final Class<?> target, final Object instance) {
        final String qualifyingName = qualifyingName(target);
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
        final String qualifyingName = qualifyingName(target);

        return registry.keySet().stream()
            .filter(target::isAssignableFrom)
            .findAny()
            .map(registry::get)
            .map(a -> a.get(qualifyingName));
    }

    private String qualifyingName(final Class<?> target) {
        String qualifyingName = target.getAnnotation(Component.class).value();
        // TODO delegate naming
        if (qualifyingName.isBlank()) {
            qualifyingName = target.getSimpleName();
            char[] nameChars = qualifyingName.toCharArray();
            nameChars[0] = Character.toLowerCase(nameChars[0]);
            qualifyingName = new String(nameChars);
        }

        return qualifyingName;
    }
}
