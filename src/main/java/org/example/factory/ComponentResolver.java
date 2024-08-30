package org.example.factory;

import java.util.Set;

public interface ComponentResolver {

    Class<?> resolve(final Set<Class<?>> scannedComponents, final Class<?> target);
}
