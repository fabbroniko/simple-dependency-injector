package com.fabbroniko.sdi.context;

public interface ApplicationContext {

    <T> T getInstance(final Class<?> target);

    <T> T getInstance(final Class<?> target, final String qualifyingName);
}