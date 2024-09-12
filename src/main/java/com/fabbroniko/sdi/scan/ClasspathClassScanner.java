package com.fabbroniko.sdi.scan;

import java.io.File;
import java.net.URL;
import java.util.Set;

public class ClasspathClassScanner implements ClassScanner {

    private final ContentFactory contentFactory;
    private final ResourceLocator resourceLocator;
    private final FileFactory fileFactory;

    public ClasspathClassScanner(final ContentFactory contentFactory,
                                 final ResourceLocator resourceLocator,
                                 final FileFactory fileFactory) {

        this.contentFactory = contentFactory;
        this.resourceLocator = resourceLocator;
        this.fileFactory = fileFactory;
    }

    @Override
    public Set<Class<?>> get(final String rootPackage) {
        final String rootPackageRelativePath = rootPackage.replace('.', '/');
        final URL resource = resourceLocator.locate(rootPackageRelativePath);
        final File rootDirectory = fileFactory.create(resource);

        return contentFactory.createDirectory().getClasses(rootPackage, rootDirectory);
    }
}
