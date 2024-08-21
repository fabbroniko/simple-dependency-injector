package org.example.scan;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

public class AnnotationPresentPredicate implements Predicate<Class<?>> {

    private final Class<? extends Annotation> annotation;

    public AnnotationPresentPredicate(final Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean test(final Class<?> clazz) {
        return clazz.isAnnotationPresent(annotation);
    }
}
