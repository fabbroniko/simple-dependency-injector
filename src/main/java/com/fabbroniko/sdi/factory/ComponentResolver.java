package com.fabbroniko.sdi.factory;

import java.util.Set;

public interface ComponentResolver {

    <T> Class<T> resolve(final Set<Class<?>> scannedComponents, final Class<T> target, final String qualifyingName);
}
