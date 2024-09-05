package org.example.context;

import org.example.exception.CircularDependencyException;
import org.example.factory.ComponentFactory;
import org.example.factory.ComponentResolver;
import org.example.naming.QualifierResolver;
import org.example.registry.Registry;

import java.util.Optional;
import java.util.Set;

public class ApplicationContextImpl implements ApplicationContext {

    private final Set<Class<?>> scannedComponents;
    private final Registry registry;
    private final ComponentFactory componentFactory;
    private final ComponentResolver componentResolver;
    private final QualifierResolver<Class<?>> nameResolver;

    public ApplicationContextImpl(final Set<Class<?>> scannedComponents,
                                  final Registry registry,
                                  final ComponentFactory componentFactory,
                                  final ComponentResolver componentResolver,
                                  final QualifierResolver<Class<?>> nameResolver) {

        this.scannedComponents = scannedComponents;
        this.registry = registry;
        this.componentFactory = componentFactory;
        this.componentResolver = componentResolver;
        this.nameResolver = nameResolver;
    }

    @Override
    public <T> T getInstance(final Class<?> target) {
        return getInstance(target, nameResolver.resolve(target));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<?> target, final String qualifyingName) {
        if (target.equals(ApplicationContext.class)) {
            return (T) this;
        }

        final Class<?> targetClass = componentResolver.resolve(scannedComponents, target, qualifyingName);
        if (registry.isProcessing(targetClass)) {
            throw new CircularDependencyException("Component %s is already processing.".formatted(target.getName()));
        }

        final Optional<Object> registeredInstance = registry.getInstance(targetClass);
        if(registeredInstance.isPresent()) {
            return (T) registeredInstance.get();
        }

        final Optional<Object> instance = registry.getInstance(targetClass);
        if(instance.isEmpty()) {
            registry.process(targetClass);
            final Object createdInstance = componentFactory.create(targetClass, this);
            registry.insert(targetClass, createdInstance);
            return (T) createdInstance;
        }

        return (T) instance.get();
    }
}