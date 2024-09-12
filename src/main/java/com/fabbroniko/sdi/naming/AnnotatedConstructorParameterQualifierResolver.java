package com.fabbroniko.sdi.naming;

import com.fabbroniko.sdi.annotation.Qualifier;

import java.lang.reflect.Parameter;

public class AnnotatedConstructorParameterQualifierResolver implements QualifierResolver<Parameter> {

    private final QualifierResolver<Parameter> fallbackResolver;

    public AnnotatedConstructorParameterQualifierResolver(final QualifierResolver<Parameter> fallbackResolver) {
        this.fallbackResolver = fallbackResolver;
    }

    @Override
    public String resolve(final Parameter constructorParameter) {
        if (constructorParameter.isAnnotationPresent(Qualifier.class)) {
            return constructorParameter.getAnnotation(Qualifier.class).value();
        }

        return fallbackResolver.resolve(constructorParameter);
    }
}
