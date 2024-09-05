package org.example.factory;

public class TypeBasedComponentResolver implements ComponentResolver {

    private final ComponentResolver fallbackComponentResolver;

    public TypeBasedComponentResolver(final ComponentResolver fallbackComponentResolver) {
        this.fallbackComponentResolver = fallbackComponentResolver;
    }

    @Override
    public Class<?> resolve(final Class<?> target, final String qualifyingName) {
        if (!target.isInterface()) {
            return target;
        }

        return fallbackComponentResolver.resolve(target, qualifyingName);
    }
}
