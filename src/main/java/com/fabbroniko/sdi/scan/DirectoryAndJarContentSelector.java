package com.fabbroniko.sdi.scan;

import java.net.URL;

public class DirectoryAndJarContentSelector implements ContentSelector {

    private final ContentFactory contentFactory;

    public DirectoryAndJarContentSelector(final ContentFactory contentFactory) {
        this.contentFactory = contentFactory;
    }

    @Override
    public FileSystemContent select(final URL resourceLocation) {
        if(resourceLocation.getProtocol().equals("jar")) {
            return contentFactory.createJar();
        }

        return contentFactory.createDirectory();
    }
}
