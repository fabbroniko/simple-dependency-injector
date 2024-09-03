package org.example.factory;

import org.example.context.ApplicationContext;
import org.example.exception.InvalidComponentConstructorException;
import org.example.naming.QualifierResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static java.util.Arrays.stream;

public class ComponentFactoryImpl implements ComponentFactory {

    private final QualifierResolver<Parameter> constructorParameterNameResolver;

    public ComponentFactoryImpl(final QualifierResolver<Parameter> constructorParameterNameResolver) {
        this.constructorParameterNameResolver = constructorParameterNameResolver;
    }

    @Override
    public Object create(final Class<?> target, final ApplicationContext context) {
        final Constructor<?> constructor = stream(target.getConstructors())
            .findAny()
            .orElseThrow(InvalidComponentConstructorException::new);

        final Parameter[] params = constructor.getParameters();
        final List<Object> vals = stream(params)
                .map(parameter -> context.getInstance(parameter.getType(), constructorParameterNameResolver.resolve(parameter)))
                .toList();

        try {
            return constructor.newInstance(vals.toArray());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
