package org.example.scan;

import java.util.Set;
import java.util.stream.Collectors;

public class GenericAnnotationScanner implements AnnotationScanner {

    private final ClassScanner classScanner;
    private final AnnotationPresentPredicate annotationPresentPredicate;

    public GenericAnnotationScanner(final ClassScanner classScanner,
                                    final AnnotationPresentPredicate annotationPresentPredicate) {
        this.classScanner = classScanner;
        this.annotationPresentPredicate = annotationPresentPredicate;
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(final String rootPackage) {
        return classScanner.get(rootPackage)
            .stream()
            .filter(annotationPresentPredicate)
            .collect(Collectors.toSet());
    }
}
