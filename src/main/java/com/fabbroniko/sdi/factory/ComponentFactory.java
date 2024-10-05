package com.fabbroniko.sdi.factory;

import com.fabbroniko.sdi.context.ApplicationContext;

public interface ComponentFactory {

    <T> T create(final Class<T> target, final ApplicationContext context);
}
