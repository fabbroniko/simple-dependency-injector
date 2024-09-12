package com.fabbroniko.sdi.scan;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class URIFileFactory implements FileFactory {

    @Override
    public File create(final URL url) {
        try {
            return new File(url.toURI());
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
