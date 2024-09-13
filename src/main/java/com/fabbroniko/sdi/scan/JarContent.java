package com.fabbroniko.sdi.scan;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JarContent implements FileSystemContent {

    private final FileFactory fileFactory;
    private final ResourceLocator resourceLocator;
    private final ClassLoaderWrapper classLoaderWrapper;

    public JarContent(final FileFactory fileFactory,
                      final ResourceLocator resourceLocator,
                      final ClassLoaderWrapper classLoaderWrapper) {
        this.fileFactory = fileFactory;
        this.resourceLocator = resourceLocator;
        this.classLoaderWrapper = classLoaderWrapper;
    }

    @Override
    public Set<Class<?>> getClasses(final String inPackage, final URL target) {
        return getClasses(inPackage, fileFactory.create(resourceLocator.locate(target.toString())));
    }

    @Override
    public Set<Class<?>> getClasses(final String inPackage, final File file) {
        final String relativeDirectory = inPackage.replace('.', '/');
        final String jarPath = file.getAbsolutePath();

        // TODO external file to jar
        try (JarFile jarFile = new JarFile(jarPath)){
            return Collections.list(jarFile.entries())
                .stream()
                .map(JarEntry::getName)
                .filter(entryName -> entryName.startsWith(relativeDirectory))
                .filter(entryName -> entryName.endsWith(".class"))
                .map(entryName -> entryName.replace('/', '.').replace('\\', '.').replace(".class", ""))
                .map(classLoaderWrapper::forName)
                .collect(Collectors.toSet());

        } catch (final Exception e) {
            throw new RuntimeException("ClassNotFoundException loading ");
        }
    }
}
