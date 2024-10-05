package com.fabbroniko.sdi.naming;

import com.fabbroniko.sdi.exception.DependencyResolutionException;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

public class ConstructorParameterQualifierResolver implements QualifierResolver<Parameter> {

    private final QualifierResolver<Class<?>> classBasedNameResolver;
    private final Set<Class<?>> scannedComponents;

    public ConstructorParameterQualifierResolver(final QualifierResolver<Class<?>> classBasedNameResolver,
                                                 final Set<Class<?>> scannedComponents) {
        this.classBasedNameResolver = classBasedNameResolver;
        this.scannedComponents = scannedComponents;
    }

    @Override
    public String resolve(final Parameter constructorParameter) {
        final Class<?> target = constructorParameter.getType();
        final List<Class<?>> matchingClasses = scannedComponents.stream()
            .filter(target::isAssignableFrom)
            .toList();

        if (matchingClasses.size() > 1) {
            throw new DependencyResolutionException("More than one class of type %s found. Use @Qualifier to specify the name of the component to use for %s"
                .formatted(target.getName(), constructorParameter.getDeclaringExecutable()));
        }

        return classBasedNameResolver.resolve(matchingClasses.stream().findFirst().orElseThrow(
            () -> new DependencyResolutionException("Could not resolve dependency %s of %s"
                .formatted(target.getName(), constructorParameter.getDeclaringExecutable()))
        ));
    }
}
