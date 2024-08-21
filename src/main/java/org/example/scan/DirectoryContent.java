package org.example.scan;

import java.io.File;
import java.util.Set;

public interface DirectoryContent {

    Set<Class<?>> getClasses(final String inPackage, final File file);
}
