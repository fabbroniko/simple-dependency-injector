package com.fabbroniko.sdi.factory;

import com.fabbroniko.sdi.context.ApplicationContext;
import com.fabbroniko.sdi.exception.InvalidComponentConstructorException;
import com.fabbroniko.sdi.naming.QualifierResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static java.util.Arrays.stream;

public class DefaultComponentFactory implements ComponentFactory {

    private final QualifierResolver<Parameter> nameResolver;

    public DefaultComponentFactory(final QualifierResolver<Parameter> nameResolver) {
        this.nameResolver = nameResolver;
    }

    @Override
    public Object create(final Class<?> target, final ApplicationContext context) {
        final Constructor<?> constructor = stream(target.getConstructors())
            .findAny()
            .orElseThrow(InvalidComponentConstructorException::new);

        final Parameter[] params = constructor.getParameters();
        final List<Object> vals = stream(params)
                .map(parameter -> parameterToInstance(parameter, context))
                .toList();

        try {
            return constructor.newInstance(vals.toArray());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object parameterToInstance(final Parameter parameter, final ApplicationContext context) {
        if(parameter.getType().isAssignableFrom(ApplicationContext.class)) {
            return context.getInstance(ApplicationContext.class);
        }

        return context.getInstance(parameter.getType(), nameResolver.resolve(parameter));
    }
}
