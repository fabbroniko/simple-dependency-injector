package org.example.factory;

import org.example.context.ApplicationContext;
import org.example.registry.Registry;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ComponentFactoryImpl implements ComponentFactory {

    private final ComponentResolver componentResolver;
    private final Registry registry;
    private final Set<Class<?>> scannedComponents;
    private final Validator validator;

    public ComponentFactoryImpl(final ComponentResolver componentResolver,
                                final Registry registry,
                                final Set<Class<?>> scannedComponents,
                                final Validator validator) {
        this.componentResolver = componentResolver;
        this.registry = registry;
        this.scannedComponents = scannedComponents;
        this.validator = validator;
    }

    @Override
    public Object create(final Class<?> clazz, final ApplicationContext context) {
        final Class<?> targetClass = componentResolver.resolve(scannedComponents, clazz);
        validator.validate(registry, scannedComponents, targetClass);

        registry.process(clazz);
        registry.process(targetClass);

        final Constructor<?>[] constructors = targetClass.getConstructors();
        final Constructor<?> constructor = constructors[0];
        final Class<?>[] params = constructor.getParameterTypes();
        final List<Object> vals = Arrays.stream(params)
                .map(context::getInstance)
                .toList();

        try {
            final Object instance = constructor.newInstance(vals.toArray());
            registry.insert(clazz, instance);
            registry.insert(targetClass, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
