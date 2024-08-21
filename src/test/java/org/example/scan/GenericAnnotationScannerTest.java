package org.example.scan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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
    @InjectMocks
    private GenericAnnotationScanner scanner;

    @BeforeEach
    void setUp() {
        when(classScanner.get(anyString())).thenReturn(Set.of(Object.class, Integer.class, String.class));
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
    void shouldReturnFilteredValues() {
        when(annotationPresentPredicate.test(Object.class)).thenReturn(true);
        when(annotationPresentPredicate.test(Integer.class)).thenReturn(false);
        when(annotationPresentPredicate.test(String.class)).thenReturn(true);

        assertThat(scanner.getAnnotatedClasses(PACKAGE))
            .containsExactlyInAnyOrder(Object.class, String.class);
    }
}