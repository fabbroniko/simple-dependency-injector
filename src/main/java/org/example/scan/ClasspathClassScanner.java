package org.example.scan;

import java.net.URL;
import java.util.Set;

public class ClasspathClassScanner implements ClassScanner {

    private final ContentSelector contentSelector;
    private final ResourceLocator resourceLocator;

    public ClasspathClassScanner(final ContentSelector contentSelector,
                                 final ResourceLocator resourceLocator) {

        this.contentSelector = contentSelector;
        this.resourceLocator = resourceLocator;
    }

    @Override
    public Set<Class<?>> get(final String rootPackage) {
        final String rootPackageRelativePath = rootPackage.replace('.', '/');
        final URL resource = resourceLocator.locate(rootPackageRelativePath);

        return contentSelector.select(resource).getClasses(rootPackage, resource);
    }
}
