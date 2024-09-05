package org.example.factory;

import org.example.exception.InvalidDependencyException;

import java.util.Set;

public class AssignableComponentResolver implements ComponentResolver {

    private final Set<Class<?>> scannedComponents;

    public AssignableComponentResolver(final Set<Class<?>> scannedComponents) {
        this.scannedComponents = scannedComponents;
    }

    @Override
    public Class<?> resolve(final Class<?> target, final String qualifyingName) {
        return scannedComponents.stream()
                .filter(target::isAssignableFrom)
                .findAny()
                .orElseThrow(() ->
                    new InvalidDependencyException("Could not resolve matching dependency of %s with qualifying name %s."
                    .formatted(target.getName(), qualifyingName)));
    }
}
