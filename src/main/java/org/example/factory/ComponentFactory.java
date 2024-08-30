package org.example.factory;

import org.example.context.ApplicationContext;
import org.example.registry.Registry;

import java.util.Set;

public interface ComponentFactory {

    Object create(final Registry registry, final Set<Class<?>> scannedComponents, final Class<?> clazz, final ApplicationContext context);
}
