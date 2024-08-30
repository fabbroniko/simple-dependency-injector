package org.example.context;

import org.example.factory.ComponentFactory;
import org.example.registry.Registry;

import java.util.Set;

public class ApplicationContextImpl implements ApplicationContext {

    private final Registry registry;
    private final Set<Class<?>> annotatedClasses;
    private final ComponentFactory componentFactory;

    public ApplicationContextImpl(final Registry registry,
                                  final Set<Class<?>> annotatedClasses,
                                  final ComponentFactory componentFactory) {

        this.registry = registry;
        this.annotatedClasses = annotatedClasses;
        this.componentFactory = componentFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<?> target) {
        return (T) registry.getInstance(target)
            .orElseGet(() -> componentFactory.create(registry, annotatedClasses, target, this));
    }
}
