package org.example.factory;

import org.example.exception.InvalidDependencyException;

import java.util.Set;

public class AssignableComponentResolver implements ComponentResolver {

    @Override
    public Class<?> resolve(final Set<Class<?>> scannedComponents, final Class<?> target, final String qualifyingName) {
        return scannedComponents.stream()
                .filter(target::isAssignableFrom)
                .findAny()
                .orElseThrow(() ->
                    new InvalidDependencyException("Could not resolve matching dependency of %s with qualifying name %s."
                    .formatted(target.getName(), qualifyingName)));
    }
}
