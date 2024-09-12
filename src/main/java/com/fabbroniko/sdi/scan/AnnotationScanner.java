package com.fabbroniko.sdi.scan;

import java.util.Set;

public interface AnnotationScanner {

    Set<Class<?>> getAnnotatedClasses(final String rootPackage);
}
