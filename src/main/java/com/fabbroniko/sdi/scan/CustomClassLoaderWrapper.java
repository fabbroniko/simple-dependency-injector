package com.fabbroniko.sdi.scan;

public class CustomClassLoaderWrapper implements ClassLoaderWrapper {

    private final ClassLoader classLoader;

    public CustomClassLoaderWrapper(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Class<?> forName(final String className) {
        try {
            return Class.forName(className, true, classLoader);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
