package org.example.naming;

import org.example.annotation.Component;

public class AnnotationBasedQualifierResolver implements QualifierResolver {

    private final QualifierResolver alternateResolver;
    private final Validator validator;

    public AnnotationBasedQualifierResolver(final QualifierResolver alternateResolver, final Validator validator) {
        this.alternateResolver = alternateResolver;
        this.validator = validator;
    }

    @Override
    public String resolve(final Class<?> target) {
        final Component targetAnnotation = target.getAnnotation(Component.class);
        if(targetAnnotation != null && validator.isValid(targetAnnotation.value())) {
            return targetAnnotation.value();
        }

        return alternateResolver.resolve(target);
    }
}
