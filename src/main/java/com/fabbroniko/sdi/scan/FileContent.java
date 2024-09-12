package com.fabbroniko.sdi.scan;

import java.io.File;
import java.util.Set;

public class FileContent implements FileSystemContent {

    private final ClassLoaderWrapper classLoaderWrapper;

    public FileContent(ClassLoaderWrapper classLoaderWrapper) {
        this.classLoaderWrapper = classLoaderWrapper;
    }

    @Override
    public Set<Class<?>> getClasses(final String inPackage, final File file) {
        final String fileName = file.getName();
        if(!fileName.endsWith(".class")) {
            return Set.of();
        }

        String className = inPackage + '.' + fileName.substring(0, fileName.length() - ".class".length());
        return Set.of(classLoaderWrapper.forName(className));
    }
}
