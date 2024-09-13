package com.fabbroniko.sdi.scan;

import java.io.File;
import java.net.URL;
import java.util.Set;

public class FileContent implements FileSystemContent {

    private final ClassLoaderWrapper classLoaderWrapper;
    private final FileFactory fileFactory;

    public FileContent(final ClassLoaderWrapper classLoaderWrapper,
                       final FileFactory fileFactory) {
        this.classLoaderWrapper = classLoaderWrapper;
        this.fileFactory = fileFactory;
    }

    @Override
    public Set<Class<?>> getClasses(final String inPackage, final URL resource) {
        return getClasses(inPackage, fileFactory.create(resource));
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
