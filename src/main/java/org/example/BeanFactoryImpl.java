package org.example;

import org.example.exception.MissingDependencyException;
import org.example.registry.Registry;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BeanFactoryImpl implements BeanFactory {

    @Override
    public Optional<Object> create(final Registry registry, final Class<?> clazz) {
        final Constructor<?> constructor = clazz.getConstructors()[0];
        final Class<?>[] params = constructor.getParameterTypes();
        final List<Object> vals;
        try {
            vals = Arrays.stream(params)
                .map(m -> registry.getInstance(m).orElseThrow(MissingDependencyException::new))
                .toList();
        } catch (MissingDependencyException e) {
            return Optional.empty();
        }

        try {
            final Object instance = constructor.newInstance(vals.toArray());
            registry.insert(clazz, instance);
            return Optional.of(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
