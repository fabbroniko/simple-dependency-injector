package org.example.factory;

import org.example.exception.CircularDependencyException;
import org.example.exception.InvalidComponentConstructorException;
import org.example.exception.InvalidDependencyException;
import org.example.registry.Registry;

import java.lang.reflect.Constructor;
import java.util.Set;

public class ComponentFactoryValidator implements Validator {

    private static final int COMPONENT_CONSTRUCTORS_ALLOWED = 1;

    @Override
    public void validate(final Registry registry, final Set<Class<?>> scannedComponents, final Class<?> target) {
        if (!scannedComponents.contains(target)) {
            throw new InvalidDependencyException();
        }

        if (registry.isProcessing(target)) {
            throw new CircularDependencyException();
        }

        final Constructor<?>[] constructors = target.getConstructors();
        if(constructors.length != COMPONENT_CONSTRUCTORS_ALLOWED) {
            throw new InvalidComponentConstructorException();
        }
    }
}
