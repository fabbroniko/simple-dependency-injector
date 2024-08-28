package org.example.registry;

import java.util.Optional;

public interface Registry {

    void process(final Class<?> target);

    void insert(final Class<?> target, final Object instance);

    Optional<Object> getInstance(final Class<?> clazz);

    boolean isProcessing(final Class<?> target);
}
