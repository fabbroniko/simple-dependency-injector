package org.example.factory;

import org.example.naming.QualifierResolver;

import java.util.Set;

public class NameBasedComponentResolver implements ComponentResolver {

    private final QualifierResolver<Class<?>> nameResolver;
    private final ComponentResolver fallbackComponentResolver;

    public NameBasedComponentResolver(final QualifierResolver<Class<?>> nameResolver,
                                      final ComponentResolver fallbackComponentResolver) {
        this.nameResolver = nameResolver;
        this.fallbackComponentResolver = fallbackComponentResolver;
    }

    @Override
    public Class<?> resolve(final Set<Class<?>> scannedComponents, final Class<?> target, final String qualifyingName) {
        return scannedComponents.stream()
            .filter(target::isAssignableFrom)
            .filter(component -> nameResolver.resolve(component).equals(qualifyingName))
            .findAny().orElseGet(() -> fallbackComponentResolver.resolve(scannedComponents, target, qualifyingName));
    }
}
