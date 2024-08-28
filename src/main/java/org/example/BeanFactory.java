package org.example;

import org.example.registry.Registry;

import java.util.Optional;
import java.util.Set;

public interface BeanFactory {

    Object create(final Registry registry, final Set<Class<?>> definedControllers, final Class<?> clazz);
}
