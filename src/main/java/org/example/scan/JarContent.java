package org.example.scan;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarContent implements FileSystemContent {

    private final FileFactory fileFactory;
    private final ResourceLocator resourceLocator;

    public JarContent(final FileFactory fileFactory,
                      final ResourceLocator resourceLocator) {
        this.fileFactory = fileFactory;
        this.resourceLocator = resourceLocator;
    }

    @Override
    public Set<Class<?>> getClasses(final String inPackage, final URL target) {
        return getClasses(inPackage, fileFactory.create(resourceLocator.locate(target.toString())));
    }

    // TODO
    @Override
    public Set<Class<?>> getClasses(final String inPackage, final File file) {
        final Set<Class<?>> classes = new HashSet<>();
        final String relativeDirectory = inPackage.replace('.', '/');
        final String jarPath = file.getAbsolutePath();

        try (JarFile jarFile = new JarFile(jarPath)){
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if(entryName.startsWith(relativeDirectory) && entryName.endsWith(".class")) {
                    String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
                    try {
                        classes.add(Class.forName(className));
                    }
                    catch (ClassNotFoundException e) {
                        throw new RuntimeException("ClassNotFoundException loading " + className);
                    }
                }
            }
        } catch (final Exception e) {

        }

        return classes;
    }
}
