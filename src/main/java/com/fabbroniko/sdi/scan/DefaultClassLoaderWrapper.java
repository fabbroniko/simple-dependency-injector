package com.fabbroniko.sdi.scan;

public class DefaultClassLoaderWrapper implements ClassLoaderWrapper {

    @Override
    public Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
