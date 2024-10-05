package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.annotation.Prototype;

public class AnnotationBasedStrategySelector implements StrategySelector {

    private final Strategy singletonStrategy;
    private final Strategy prototypeStrategy;

    public AnnotationBasedStrategySelector(final Strategy singletonStrategy, final Strategy prototypeStrategy) {
        this.singletonStrategy = singletonStrategy;
        this.prototypeStrategy = prototypeStrategy;
    }

    @Override
    public Strategy select(final Class<?> target) {
        if(target.isAnnotationPresent(Prototype.class)) {
            return prototypeStrategy;
        }

        return singletonStrategy;
    }
}
