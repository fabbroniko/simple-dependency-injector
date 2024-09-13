package com.fabbroniko.sdi.factory;

import java.util.Set;

public interface ComponentResolver {

    Class<?> resolve(final Set<Class<?>> scannedComponents, final Class<?> target, final String qualifyingName);
}
