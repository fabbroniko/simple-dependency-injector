package org.example.registry;

import java.util.Optional;

public interface Registry {

    void insert(final Class<?> clazz, final Object instance);

    Optional<Object> getInstance(final Class<?> clazz);
}
