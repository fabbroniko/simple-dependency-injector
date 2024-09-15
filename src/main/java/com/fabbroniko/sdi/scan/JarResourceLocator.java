package com.fabbroniko.sdi.scan;

import java.net.URL;

public class JarResourceLocator implements ResourceLocator {

    private final ResourceLocator stringToUrlLocator;

    public JarResourceLocator(final ResourceLocator stringToUrlLocator) {
        this.stringToUrlLocator = stringToUrlLocator;
    }

    @Override
    public URL locate(final String target) {
        String sanitizedLocation = target;
        if (target.contains("!")) {
            sanitizedLocation = sanitizedLocation.substring(0, target.indexOf("!"));
        }
        if(sanitizedLocation.startsWith("jar:")) {
            sanitizedLocation = sanitizedLocation.substring(4);
        }

        return stringToUrlLocator.locate(sanitizedLocation);
    }
}