package org.example.factory;

import org.example.context.ApplicationContext;

public interface ComponentFactory {

    Object create(final Class<?> target, final ApplicationContext context);
}
