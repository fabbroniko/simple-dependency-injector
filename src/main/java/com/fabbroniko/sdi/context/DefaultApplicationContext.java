package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.exception.CircularDependencyException;
import com.fabbroniko.sdi.factory.ComponentFactory;
import com.fabbroniko.sdi.factory.ComponentResolver;
import com.fabbroniko.sdi.naming.QualifierResolver;
import com.fabbroniko.sdi.registry.Registry;
import com.fabbroniko.ul.Logger;
import com.fabbroniko.ul.manager.LogManager;

import java.util.Optional;
import java.util.Set;

public class DefaultApplicationContext implements ApplicationContext {

    private final Logger logger;
    private final Set<Class<?>> scannedComponents;
    private final Registry registry;
    private final ComponentFactory componentFactory;
    private final ComponentResolver componentResolver;
    private final QualifierResolver<Class<?>> nameResolver;

    public DefaultApplicationContext(final LogManager logManager,
                                     final Set<Class<?>> scannedComponents,
                                     final Registry registry,
                                     final ComponentFactory componentFactory,
                                     final ComponentResolver componentResolver,
                                     final QualifierResolver<Class<?>> nameResolver) {
        this.logger = logManager.getLogger(DefaultApplicationContext.class);
        this.scannedComponents = scannedComponents;
        this.registry = registry;
        this.componentFactory = componentFactory;
        this.componentResolver = componentResolver;
        this.nameResolver = nameResolver;
    }

    @Override
    public <T> T getInstance(final Class<?> target) {
        return getInstance(target, nameResolver.resolve(target));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<?> target, final String qualifyingName) {
        logger.trace("get_instance", target.getName(), qualifyingName);
        if (target.equals(ApplicationContext.class)) {
            return (T) this;
        }

        final Class<?> targetClass = componentResolver.resolve(scannedComponents, target, qualifyingName);
        logger.trace("resolve_target_class", target.getName(), qualifyingName, targetClass.getName());
        if (registry.isProcessing(targetClass)) {
            logger.fatal("circular_dependency", targetClass.getName());
            throw new CircularDependencyException("Component %s is already processing.".formatted(targetClass.getName()));
        }

        final Optional<Object> registeredInstance = registry.getInstance(targetClass);
        if(registeredInstance.isPresent()) {
            logger.trace("target_found_in_registry", target.getName());
            return (T) registeredInstance.get();
        }

        registry.process(targetClass);
        logger.trace("create_new_instance", targetClass.getName());
        final Object createdInstance = componentFactory.create(targetClass, this);
        registry.insert(targetClass, createdInstance);
        return (T) createdInstance;
    }
}