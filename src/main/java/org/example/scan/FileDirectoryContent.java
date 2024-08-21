package org.example.scan;

import java.io.File;
import java.util.Set;

public class FileDirectoryContent implements DirectoryContent {

    private final ClassLoaderWrapper classLoaderWrapper;

    public FileDirectoryContent(ClassLoaderWrapper classLoaderWrapper) {
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
