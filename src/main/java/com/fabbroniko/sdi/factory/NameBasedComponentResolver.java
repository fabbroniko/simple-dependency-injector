package com.fabbroniko.sdi.factory;

import com.fabbroniko.sdi.naming.QualifierResolver;

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
    @SuppressWarnings("unchecked")
    public <T> Class<T> resolve(final Set<Class<?>> scannedComponents, final Class<T> target, final String qualifyingName) {
        return (Class<T>) scannedComponents.stream()
            .filter(target::isAssignableFrom)
            .filter(component -> nameResolver.resolve(component).equals(qualifyingName))
            .findAny().orElseGet(() -> fallbackComponentResolver.resolve(scannedComponents, target, qualifyingName));
    }
}
