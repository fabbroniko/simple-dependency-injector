package com.fabbroniko.sdi.factory;

import com.fabbroniko.sdi.exception.InvalidDependencyException;

import java.util.Set;

public class AssignableComponentResolver implements ComponentResolver {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> resolve(final Set<Class<?>> scannedComponents, final Class<T> target, final String qualifyingName) {
        return (Class<T>) scannedComponents.stream()
                .filter(target::isAssignableFrom)
                .findAny()
                .orElseThrow(() ->
                    new InvalidDependencyException("Could not resolve matching dependency of %s with qualifying name %s."
                    .formatted(target.getName(), qualifyingName)));
    }
}
