package org.example.factory;

import org.example.context.ApplicationContext;
import org.example.exception.CircularDependencyException;
import org.example.exception.InvalidComponentConstructorException;
import org.example.exception.InvalidDependencyException;
import org.example.registry.Registry;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ComponentFactoryImpl implements ComponentFactory {

    private static final int COMPONENT_CONSTRUCTORS_ALLOWED = 1;

    private final ComponentResolver componentResolver;

    public ComponentFactoryImpl(final ComponentResolver componentResolver) {
        this.componentResolver = componentResolver;
    }

    /* Responsibilities
     * Validation (3)
     */

    @Override
    public Object create(final Registry registry, final Set<Class<?>> scannedComponents, final Class<?> clazz, final ApplicationContext context) {
        final Class<?> targetClass = componentResolver.resolve(scannedComponents, clazz);

        if (!scannedComponents.contains(targetClass)) {
            throw new InvalidDependencyException();
        }

        if (registry.isProcessing(targetClass)) {
            throw new CircularDependencyException();
        }

        registry.process(clazz);

        final Constructor<?>[] constructors = targetClass.getConstructors();
        if(constructors.length != COMPONENT_CONSTRUCTORS_ALLOWED) {
            throw new InvalidComponentConstructorException();
        }

        final Constructor<?> constructor = constructors[0];
        final Class<?>[] params = constructor.getParameterTypes();
        final List<Object> vals = Arrays.stream(params)
                .map(context::getInstance)
                .toList();

        try {
            final Object instance = constructor.newInstance(vals.toArray());
            registry.insert(clazz, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
