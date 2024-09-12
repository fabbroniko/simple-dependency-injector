package com.fabbroniko.sdi.scan;

public interface ClassLoaderWrapper {

    Class<?> forName(final String name);
}
