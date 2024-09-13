package com.fabbroniko.sdi.scan;

import com.fabbroniko.sdi.annotation.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationPresentPredicateTest {

    private AnnotationPresentPredicate annotationPresentPredicate;

    @BeforeEach
    void setUp() {
        annotationPresentPredicate = new AnnotationPresentPredicate(Component.class);
    }

    @Test
    void shouldReturnTrueForAnnotatedClasses() {
        assertThat(annotationPresentPredicate.test(TargetClass.class))
            .isTrue();
    }

    @Test
    void shouldReturnFalseForNonAnnotatedClasses() {
        assertThat(annotationPresentPredicate.test(String.class))
            .isFalse();
    }

    @Component
    private static class TargetClass {
    }
}