package org.example.scan;

import java.util.Set;

public interface AnnotationScanner {

    Set<Class<?>> getAnnotatedClasses(final String rootPackage);
}
