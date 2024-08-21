package org.example.scan;

public interface ClassLoaderWrapper {

    Class<?> forName(final String name);
}
