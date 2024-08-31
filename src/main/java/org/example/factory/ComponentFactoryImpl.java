package org.example.factory;

import org.example.context.ApplicationContext;
import org.example.exception.InvalidComponentConstructorException;

import java.lang.reflect.Constructor;
import java.util.List;

import static java.util.Arrays.stream;

public class ComponentFactoryImpl implements ComponentFactory {

    @Override
    public Object create(final Class<?> target, final ApplicationContext context) {
        final Constructor<?> constructor = stream(target.getConstructors())
            .findAny()
            .orElseThrow(InvalidComponentConstructorException::new);

        final Class<?>[] params = constructor.getParameterTypes();
        final List<Object> vals = stream(params)
                .map(context::getInstance)
                .toList();

        try {
            return constructor.newInstance(vals.toArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
