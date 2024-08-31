package org.example.context;

public interface ApplicationContext {

    <T> T getInstance(Class<?> target);
}
