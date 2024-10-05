package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.exception.ComponentDefinitionException;
import com.fabbroniko.sdi.factory.ComponentResolver;
import com.fabbroniko.sdi.naming.QualifierResolver;
import com.fabbroniko.ul.Logger;
import com.fabbroniko.ul.manager.LogManager;

import java.util.Set;

public class DefaultApplicationContext implements ApplicationContext {

    private final Logger logger;
    private final Set<Class<?>> scannedComponents;
    private final StrategySelector strategySelector;
    private final ComponentResolver componentResolver;
    private final QualifierResolver<Class<?>> nameResolver;

    public DefaultApplicationContext(final LogManager logManager,
                                     final Set<Class<?>> scannedComponents,
                                     final StrategySelector strategySelector,
                                     final ComponentResolver componentResolver,
                                     final QualifierResolver<Class<?>> nameResolver) {
        this.logger = logManager.getLogger(DefaultApplicationContext.class);
        this.scannedComponents = scannedComponents;
        this.strategySelector = strategySelector;
        this.componentResolver = componentResolver;
        this.nameResolver = nameResolver;
    }

    @Override
    public <T> T getInstance(final Class<T> target) {
        return getInstance(target, nameResolver.resolve(target));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<T> target, final String qualifyingName) {
        logger.trace("get_instance", target.getName(), qualifyingName);
        if (target.equals(ApplicationContext.class)) {
            return (T) this;
        }

        final Class<T> targetClass = componentResolver.resolve(scannedComponents, target, qualifyingName);
        logger.trace("resolve_target_class", target.getName(), qualifyingName, targetClass.getName());

        if (!targetClass.isAnnotationPresent(Component.class)) {
            throw new ComponentDefinitionException("Resolved target class %s is not a Component"
                .formatted(targetClass.getName()));
        }

        return strategySelector.select(targetClass).getInstance(targetClass, this);
    }
}