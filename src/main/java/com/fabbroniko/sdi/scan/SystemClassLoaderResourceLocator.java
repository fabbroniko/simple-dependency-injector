package com.fabbroniko.sdi.scan;

import java.net.URL;

public class SystemClassLoaderResourceLocator implements ResourceLocator {

    @Override
    public URL locate(final String targetPackage) {
        return ClassLoader.getSystemClassLoader().getResource(targetPackage);
    }
}