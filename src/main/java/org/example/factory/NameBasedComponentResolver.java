package org.example.factory;

import org.example.naming.QualifierResolver;

import java.util.Set;

public class NameBasedComponentResolver implements ComponentResolver {

    private final Set<Class<?>> scannedComponents;
    private final QualifierResolver<Class<?>> nameResolver;
    private final ComponentResolver fallbackComponentResolver;

    public NameBasedComponentResolver(final Set<Class<?>> scannedComponents,
                                      final QualifierResolver<Class<?>> nameResolver,
                                      final ComponentResolver fallbackComponentResolver) {
        this.scannedComponents = scannedComponents;
        this.nameResolver = nameResolver;
        this.fallbackComponentResolver = fallbackComponentResolver;
    }

    @Override
    public Class<?> resolve(final Class<?> target, final String qualifyingName) {
        return scannedComponents.stream()
            .filter(target::isAssignableFrom)
            .filter(component -> nameResolver.resolve(component).equals(qualifyingName))
            .findAny().orElseGet(() -> fallbackComponentResolver.resolve(target, qualifyingName));
    }
}
