package com.fabbroniko.sdi.factory;

import com.fabbroniko.sdi.context.ApplicationContext;

public interface ComponentFactory {

    Object create(final Class<?> target, final ApplicationContext context);
}
