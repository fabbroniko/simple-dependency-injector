package com.fabbroniko.sdi.scan;

import java.io.IOException;
import java.util.jar.JarFile;

public class DefaultJarFileFactory implements JarFileFactory {

    @Override
    public JarFile create(final String path) throws IOException {
        return new JarFile(path);
    }
}
