package org.example;

import org.example.registry.Registry;

import java.util.Optional;

public interface BeanFactory {

    Optional<Object> create(final Registry registry, final Class<?> clazz);
}
