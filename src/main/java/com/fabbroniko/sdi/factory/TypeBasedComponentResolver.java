package com.fabbroniko.sdi.factory;

import java.util.Set;

public class TypeBasedComponentResolver implements ComponentResolver {

    private final ComponentResolver fallbackComponentResolver;

    public TypeBasedComponentResolver(final ComponentResolver fallbackComponentResolver) {
        this.fallbackComponentResolver = fallbackComponentResolver;
    }

    @Override
    public <T> Class<T> resolve(final Set<Class<?>> scannedComponents, final Class<T> target, final String qualifyingName) {
        if (!target.isInterface()) {
            return target;
        }

        return fallbackComponentResolver.resolve(scannedComponents, target, qualifyingName);
    }
}
