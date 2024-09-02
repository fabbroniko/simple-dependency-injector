package org.example.context;

import org.example.exception.CircularDependencyException;
import org.example.factory.ComponentFactory;
import org.example.factory.ComponentResolver;
import org.example.naming.QualifyingNameResolver;
import org.example.registry.Registry;

import java.util.Optional;

public class ApplicationContextImpl implements ApplicationContext {

    private final Registry registry;
    private final ComponentFactory componentFactory;
    private final ComponentResolver componentResolver;
    private final QualifyingNameResolver nameResolver;

    public ApplicationContextImpl(final Registry registry,
                                  final ComponentFactory componentFactory,
                                  final ComponentResolver componentResolver,
                                  final QualifyingNameResolver nameResolver) {

        this.registry = registry;
        this.componentFactory = componentFactory;
        this.componentResolver = componentResolver;
        this.nameResolver = nameResolver;
    }

    @Override
    public <T> T getInstance(final Class<?> target) {
        return getInstance(target, nameResolver.resolveFor(target));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<?> target, final String qualifyingName) {
        if (target.equals(ApplicationContext.class)) {
            return (T) this;
        }

        final Class<?> targetClass = componentResolver.resolve(target, qualifyingName);
        if (registry.isProcessing(targetClass)) {
            throw new CircularDependencyException("Component %s is already processing.".formatted(target.getName()));
        }

        registry.process(targetClass);

        final Optional<Object> instance = registry.getInstance(targetClass);
        if(instance.isEmpty()) {
            final Object createdInstance = componentFactory.create(targetClass, this);
            registry.insert(targetClass, createdInstance);
            return (T) createdInstance;
        }

        return (T) instance.get();
    }
}