package org.example;

import org.example.exception.DependencyNotInitializableException;
import org.example.registry.Registry;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BeanFactoryImpl implements BeanFactory {

    @Override
    public Object create(final Registry registry, final Set<Class<?>> definedControllers, final Class<?> clazz) {
        Class<?> targetClass = clazz;
        if (clazz.isInterface()) {
            targetClass = definedControllers.stream()
                .filter(clazz::isAssignableFrom)
                .findAny()
                .orElseThrow(DependencyNotInitializableException::new);
        }

        final Constructor<?> constructor = targetClass.getConstructors()[0];
        final Class<?>[] params = constructor.getParameterTypes();
        final List<Object> vals = Arrays.stream(params)
                .map(m -> registry.getInstance(m).orElseGet(() -> create(registry, definedControllers, m)))
                .toList();

        try {
            final Object instance = constructor.newInstance(vals.toArray());
            registry.insert(clazz, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
