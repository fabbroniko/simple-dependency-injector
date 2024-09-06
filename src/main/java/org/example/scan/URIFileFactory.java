package org.example.scan;

import java.io.File;
import java.net.URL;

public class URIFileFactory implements FileFactory {

    @Override
    public File create(final URL url) {
        try {
            return new File(url.toURI());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
