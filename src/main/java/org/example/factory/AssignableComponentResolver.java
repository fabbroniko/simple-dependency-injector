package org.example.factory;

import org.example.exception.InvalidDependencyException;
import org.example.naming.QualifyingNameResolver;

import java.util.Set;

public class AssignableComponentResolver implements ComponentResolver {

    private final Set<Class<?>> scannedComponents;
    private final QualifyingNameResolver nameResolver;

    public AssignableComponentResolver(final Set<Class<?>> scannedComponents,
                                       final QualifyingNameResolver nameResolver) {
        this.scannedComponents = scannedComponents;
        this.nameResolver = nameResolver;
    }

    @Override
    public Class<?> resolve(final Class<?> target, final String qualifyingName) {
        if (target.isInterface()) {
            return scannedComponents.stream()
                .filter(target::isAssignableFrom)
                .filter(component -> nameResolver.resolveFor(component).equals(qualifyingName))
                .findAny().orElseGet(() -> scannedComponents.stream()
                    .filter(target::isAssignableFrom)
                    .findAny()
                    .orElseThrow(() ->
                        new InvalidDependencyException("Could not resolve matching dependency of %s with qualifying name %s."
                        .formatted(target.getName(), qualifyingName))
                    ));
        }

        return target;
    }
}
