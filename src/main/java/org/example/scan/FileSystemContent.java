package org.example.scan;

import java.io.File;
import java.net.URL;
import java.util.Set;

public interface FileSystemContent {

    Set<Class<?>> getClasses(final String inPackage, final URL target);

    Set<Class<?>> getClasses(final String inPackage, final File target);
}
