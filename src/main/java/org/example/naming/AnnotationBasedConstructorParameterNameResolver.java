package org.example.naming;

import org.example.annotation.Qualifier;
import org.example.context.ApplicationContext;
import org.example.exception.DependencyResolutionException;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Set;

public class AnnotationBasedConstructorParameterNameResolver implements ConstructorParameterNameResolver {

    private final QualifierResolver classBasedNameResolver;
    private final Set<Class<?>> scannedComponents;

    public AnnotationBasedConstructorParameterNameResolver(final QualifierResolver classBasedNameResolver,
                                                           final Set<Class<?>> scannedComponents) {
        this.classBasedNameResolver = classBasedNameResolver;
        this.scannedComponents = scannedComponents;
    }

    @Override
    public String resolve(final Parameter constructorParameter) {
        final Class<?> target = constructorParameter.getType();
        if (target.isAssignableFrom(ApplicationContext.class)) {
            return null;
        } else if (constructorParameter.isAnnotationPresent(Qualifier.class)) {
            return constructorParameter.getAnnotation(Qualifier.class).value();
        }

        final List<Class<?>> matchingClasses = scannedComponents.stream()
            .filter(target::isAssignableFrom)
            .toList();

        if (matchingClasses.size() > 1) {
            throw new DependencyResolutionException("More than one class of type %s found. Use @Qualifier to specify the name of the component to use for %s"
                .formatted(target.getName(), constructorParameter.getDeclaringExecutable()));
        }

        return classBasedNameResolver.resolve(matchingClasses.stream().findFirst().orElseThrow());
    }
}
