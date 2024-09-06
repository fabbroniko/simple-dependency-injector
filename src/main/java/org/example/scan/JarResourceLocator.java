package org.example.scan;

import java.net.URL;

public class JarResourceLocator implements ResourceLocator {

    private final ResourceLocator stringToUrlLocator;

    public JarResourceLocator(final ResourceLocator stringToUrlLocator) {
        this.stringToUrlLocator = stringToUrlLocator;
    }

    @Override
    public URL locate(final String target) {
        final String sanitizedLocation = target.substring(0, target.indexOf("!")).substring(4);

        return stringToUrlLocator.locate(sanitizedLocation);
    }
}