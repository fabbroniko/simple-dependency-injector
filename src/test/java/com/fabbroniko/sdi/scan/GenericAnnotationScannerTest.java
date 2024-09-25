package com.fabbroniko.sdi.scan;

import com.fabbroniko.ul.Logger;
import com.fabbroniko.ul.manager.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenericAnnotationScannerTest {

    private static final String PACKAGE = "test-package";

    @Mock
    private ClassScanner classScanner;
    @Mock
    private AnnotationPresentPredicate annotationPresentPredicate;
    @Mock
    private LogManager logManager;
    @Mock
    private Logger logger;

    private AnnotationScanner scanner;

    @BeforeEach
    void setUp() {
        when(classScanner.get(anyString())).thenReturn(Set.of(Object.class, Integer.class, String.class));
        when(logManager.getLogger(any())).thenReturn(logger);

        scanner = new GenericAnnotationScanner(classScanner, annotationPresentPredicate, logManager);
    }

    @Test
    void shouldGetClassesFromClassScanner() {
        scanner.getAnnotatedClasses(PACKAGE);

        verify(classScanner).get(PACKAGE);
    }

    @Test
    void shouldTestPredicateForObjectClass() {
        scanner.getAnnotatedClasses(PACKAGE);

        verify(annotationPresentPredicate).test(Object.class);
    }

    @Test
    void shouldTestPredicateForIntegerClass() {
        scanner.getAnnotatedClasses(PACKAGE);

        verify(annotationPresentPredicate).test(Integer.class);
    }

    @Test
    void shouldTestPredicateForStringClass() {
        scanner.getAnnotatedClasses(PACKAGE);

        verify(annotationPresentPredicate).test(String.class);
    }

    @Test
    void shouldLogScannedClassForObject() {
        scanner.getAnnotatedClasses(PACKAGE);

        verify(logger).trace("classes_scanned", PACKAGE, "java.lang.Object");
    }

    @Test
    void shouldLogScannedClassForInteger() {
        scanner.getAnnotatedClasses(PACKAGE);

        verify(logger).trace("classes_scanned", PACKAGE, "java.lang.Integer");
    }

    @Test
    void shouldLogScannedClassForString() {
        scanner.getAnnotatedClasses(PACKAGE);

        verify(logger).trace("classes_scanned", PACKAGE, "java.lang.String");
    }

    @Test
    void shouldLogScannedAnnotationForObject() {
        when(annotationPresentPredicate.test(Object.class)).thenReturn(true);
        when(annotationPresentPredicate.test(Integer.class)).thenReturn(false);
        when(annotationPresentPredicate.test(String.class)).thenReturn(true);

        scanner.getAnnotatedClasses(PACKAGE);

        verify(logger).trace("annotated_classes_scanned", PACKAGE, "java.lang.Object");
    }

    @Test
    void shouldLogScannedAnnotationForString() {
        when(annotationPresentPredicate.test(Object.class)).thenReturn(true);
        when(annotationPresentPredicate.test(Integer.class)).thenReturn(false);
        when(annotationPresentPredicate.test(String.class)).thenReturn(true);

        scanner.getAnnotatedClasses(PACKAGE);

        verify(logger).trace("annotated_classes_scanned", PACKAGE, "java.lang.String");
    }

    @Test
    void shouldReturnFilteredValues() {
        when(annotationPresentPredicate.test(Object.class)).thenReturn(true);
        when(annotationPresentPredicate.test(Integer.class)).thenReturn(false);
        when(annotationPresentPredicate.test(String.class)).thenReturn(true);

        assertThat(scanner.getAnnotatedClasses(PACKAGE))
            .containsExactlyInAnyOrder(Object.class, String.class);
    }
}