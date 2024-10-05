package com.fabbroniko.sdi.context;

public interface Strategy {

    <T> T getInstance(final Class<T> targetClass, final ApplicationContext applicationContext);
}
