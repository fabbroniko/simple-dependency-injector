package org.example.context;

import org.example.exception.CircularDependencyException;
import org.example.exception.InvalidDependencyException;
import org.example.factory.ComponentFactory;
import org.example.factory.ComponentResolver;
import org.example.registry.Registry;

import java.util.Optional;
import java.util.Set;

public class ApplicationContextImpl implements ApplicationContext {

    private final Registry registry;
    private final ComponentFactory componentFactory;
    private final ComponentResolver componentResolver;
    private final Set<Class<?>> scannedComponents;

    public ApplicationContextImpl(final Registry registry,
                                  final ComponentFactory componentFactory,
                                  final ComponentResolver componentResolver,
                                  final Set<Class<?>> scannedComponents) {

        this.registry = registry;
        this.componentFactory = componentFactory;
        this.componentResolver = componentResolver;
        this.scannedComponents = scannedComponents;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<?> target) {
        if (target.equals(ApplicationContext.class)) {
            return (T) this;
        }

        final Class<?> targetClass = componentResolver.resolve(scannedComponents, target);
        if (!scannedComponents.contains(targetClass)) {
            throw new InvalidDependencyException();
        }

        if (registry.isProcessing(targetClass)) {
            throw new CircularDependencyException();
        }

        registry.process(target);

        final Optional<Object> instance = registry.getInstance(targetClass);
        if(instance.isEmpty()) {
            final Object createdInstance = componentFactory.create(targetClass, this);
            registry.insert(target, createdInstance);
            return (T) createdInstance;
        }

        return (T) instance.get();
    }
}