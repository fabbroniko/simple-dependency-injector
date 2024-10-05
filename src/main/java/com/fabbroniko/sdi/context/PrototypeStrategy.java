package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.factory.ComponentFactory;
import com.fabbroniko.ul.Logger;

public class PrototypeStrategy implements Strategy {

    private final Logger logger;
    private final ComponentFactory componentFactory;

    public PrototypeStrategy(final Logger logger, final ComponentFactory componentFactory) {
        this.logger = logger;
        this.componentFactory = componentFactory;
    }

    @Override
    public <T> T getInstance(final Class<T> targetClass, final ApplicationContext applicationContext) {
        logger.trace("create_prototype_target", targetClass.getName());
        return componentFactory.create(targetClass, applicationContext);
    }
}
