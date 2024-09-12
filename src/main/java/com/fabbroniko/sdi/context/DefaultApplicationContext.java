package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.exception.CircularDependencyException;
import com.fabbroniko.sdi.factory.ComponentFactory;
import com.fabbroniko.sdi.factory.ComponentResolver;
import com.fabbroniko.sdi.naming.QualifierResolver;
import com.fabbroniko.sdi.registry.Registry;

import java.util.Optional;
import java.util.Set;

public class DefaultApplicationContext implements ApplicationContext {

    private final Set<Class<?>> scannedComponents;
    private final Registry registry;
    private final ComponentFactory componentFactory;
    private final ComponentResolver componentResolver;
    private final QualifierResolver<Class<?>> nameResolver;

    public DefaultApplicationContext(final Set<Class<?>> scannedComponents,
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