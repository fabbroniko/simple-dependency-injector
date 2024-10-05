package com.fabbroniko.sdi.context;

public interface StrategySelector {

    Strategy select(final Class<?> target);
}
