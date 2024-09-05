package org.example.context;

public interface ApplicationContext {

    <T> T getInstance(final Class<?> target);

    <T> T getInstance(final Class<?> target, final String qualifyingName);
}
