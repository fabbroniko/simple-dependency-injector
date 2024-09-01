package org.example.factory;

public interface ComponentResolver {

    Class<?> resolve(final Class<?> target);

    Class<?> resolve(final Class<?> target, final String qualifyingName);
}
