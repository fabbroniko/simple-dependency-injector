package org.example.factory;

import org.example.exception.InvalidDependencyException;

import java.util.Set;

public class AssignableComponentResolver implements ComponentResolver {

    @Override
    public Class<?> resolve(final Set<Class<?>> scannedComponents, final Class<?> target) {
        if (target.isInterface()) {
            return scannedComponents.stream()
                .filter(target::isAssignableFrom)
                .findAny()
                .orElseThrow(InvalidDependencyException::new);
        }

        return target;
    }
}
