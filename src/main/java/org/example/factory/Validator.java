package org.example.factory;

import org.example.registry.Registry;

import java.util.Set;

public interface Validator {

    void validate(final Registry registry, final Set<Class<?>> scannedComponents, final Class<?> target);
}
