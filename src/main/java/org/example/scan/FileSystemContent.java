package org.example.scan;

import java.io.File;
import java.util.Set;

public interface FileSystemContent {

    Set<Class<?>> getClasses(final String inPackage, final File file);
}
