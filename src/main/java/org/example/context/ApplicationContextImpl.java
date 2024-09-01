package org.example.context;

import org.example.annotation.Component;
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
    public <T> T getInstance(final Class<?> target) {
        return getInstance(target, qualifyingName(target));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<?> target, final String qualifyingName) {
        if (target.equals(ApplicationContext.class)) {
            return (T) this;
        }

        final Class<?> targetClass = componentResolver.resolve(target, qualifyingName);
        if (!scannedComponents.contains(targetClass)) {
            throw new InvalidDependencyException();
        }

        if (registry.isProcessing(targetClass)) {
            throw new CircularDependencyException();
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