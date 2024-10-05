package com.fabbroniko.sdi.context;

public interface ApplicationContext {

    <T> T getInstance(final Class<T> target);

    <T> T getInstance(final Class<T> target, final String qualifyingName);
}
