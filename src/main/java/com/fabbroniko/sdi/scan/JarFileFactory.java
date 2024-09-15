package com.fabbroniko.sdi.scan;

import java.io.IOException;
import java.util.jar.JarFile;

public interface JarFileFactory {

    JarFile create(final String path) throws IOException;
}
