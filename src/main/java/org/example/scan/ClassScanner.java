package org.example.scan;

import java.util.Set;

public interface ClassScanner {

    Set<Class<?>> get(final String rootPackage);
}
