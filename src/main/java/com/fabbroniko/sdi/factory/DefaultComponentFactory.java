package com.fabbroniko.sdi.factory;

import com.fabbroniko.sdi.context.ApplicationContext;
import com.fabbroniko.sdi.exception.InvalidComponentConstructorException;
import com.fabbroniko.sdi.naming.QualifierResolver;
import com.fabbroniko.ul.Logger;
import com.fabbroniko.ul.manager.LogManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static java.util.Arrays.stream;

public class DefaultComponentFactory implements ComponentFactory {

    private final QualifierResolver<Parameter> nameResolver;
    private final LogManager logManager;
    private final Logger logger;

    public DefaultComponentFactory(final QualifierResolver<Parameter> nameResolver, final LogManager logManager) {
        this.nameResolver = nameResolver;
        this.logManager = logManager;
        this.logger = logManager.getLogger(DefaultComponentFactory.class);
    }

    @Override
    public Object create(final Class<?> target, final ApplicationContext context) {
        final Constructor<?> constructor = stream(target.getConstructors())
            .findAny()
            .orElseThrow(InvalidComponentConstructorException::new);

        final Parameter[] params = constructor.getParameters();
        final List<Object> vals = stream(params)
                .map(parameter -> parameterToInstance(target, parameter, context))
                .toList();

        try {
            return constructor.newInstance(vals.toArray());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object parameterToInstance(final Class<?> target, final Parameter parameter, final ApplicationContext context) {
        final Class<?> dependencyType = parameter.getType();
        logger.trace("parameter_to_instance", target.getName(), parameter.getName(), dependencyType.getName());
        if (dependencyType.isAssignableFrom(ApplicationContext.class)) {
            logger.trace("returning_context", target.getName(), parameter.getName(), dependencyType.getName());
            return context.getInstance(ApplicationContext.class);
        } else if (dependencyType.isAssignableFrom(Logger.class)) {
            logger.trace("returning_logger", target.getName(), parameter.getName(), dependencyType.getName());
            return logManager.getLogger(target);
        }

        return context.getInstance(dependencyType, nameResolver.resolve(parameter));
    }
}
