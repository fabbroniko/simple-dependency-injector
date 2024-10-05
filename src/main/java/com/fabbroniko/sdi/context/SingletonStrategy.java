package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.exception.CircularDependencyException;
import com.fabbroniko.sdi.factory.ComponentFactory;
import com.fabbroniko.sdi.registry.Registry;
import com.fabbroniko.ul.Logger;

import java.util.Optional;

public class SingletonStrategy implements Strategy {

    private final Logger logger;
    private final ComponentFactory componentFactory;
    private final Registry registry;

    public SingletonStrategy(final Logger logger,
                             final ComponentFactory componentFactory,
                             final Registry registry) {
        this.logger = logger;
        this.componentFactory = componentFactory;
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<T> targetClass, final ApplicationContext applicationContext) {
        if (registry.isProcessing(targetClass)) {
            logger.fatal("circular_dependency", targetClass.getName());
            throw new CircularDependencyException("Component %s is already processing.".formatted(targetClass.getName()));
        }

        final Optional<Object> registeredInstance = registry.getInstance(targetClass);
        if (registeredInstance.isPresent()) {
            logger.trace("target_found_in_registry", targetClass.getName());
            return (T) registeredInstance.get();
        }

        registry.process(targetClass);
        logger.trace("create_new_instance", targetClass.getName());
        final T createdInstance = componentFactory.create(targetClass, applicationContext);
        registry.insert(targetClass, createdInstance);
        return createdInstance;
    }
}
