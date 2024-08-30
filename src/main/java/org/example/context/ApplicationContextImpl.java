package org.example.context;

import org.example.factory.ComponentFactory;
import org.example.registry.Registry;

public class ApplicationContextImpl implements ApplicationContext {

    private final Registry registry;
    private final ComponentFactory componentFactory;

    public ApplicationContextImpl(final Registry registry,
                                  final ComponentFactory componentFactory) {

        this.registry = registry;
        this.componentFactory = componentFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<?> target) {
        return (T) registry.getInstance(target)
            .orElseGet(() -> componentFactory.create(target, this));
    }
}
