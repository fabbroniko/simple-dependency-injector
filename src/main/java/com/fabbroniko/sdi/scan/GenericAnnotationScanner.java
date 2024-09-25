package com.fabbroniko.sdi.scan;

import com.fabbroniko.ul.Logger;
import com.fabbroniko.ul.manager.LogManager;

import java.util.Set;
import java.util.stream.Collectors;

public class GenericAnnotationScanner implements AnnotationScanner {

    private final ClassScanner classScanner;
    private final AnnotationPresentPredicate annotationPresentPredicate;
    private final Logger logger;

    public GenericAnnotationScanner(final ClassScanner classScanner,
                                    final AnnotationPresentPredicate annotationPresentPredicate,
                                    final LogManager logManager) {
        this.classScanner = classScanner;
        this.annotationPresentPredicate = annotationPresentPredicate;
        this.logger = logManager.getLogger(GenericAnnotationScanner.class);
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(final String rootPackage) {
        return classScanner.get(rootPackage)
            .stream()
            .peek(scannedClass -> logger.trace("classes_scanned", rootPackage, scannedClass.getName()))
            .filter(annotationPresentPredicate)
            .peek(annotatedClass -> logger.trace("annotated_classes_scanned", rootPackage, annotatedClass.getName()))
            .collect(Collectors.toSet());
    }
}
