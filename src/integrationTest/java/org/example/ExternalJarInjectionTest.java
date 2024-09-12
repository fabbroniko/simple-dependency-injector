package org.example;

import org.example.context.ApplicationContext;
import org.example.context.DefaultApplicationContext;
import org.example.factory.AssignableComponentResolver;
import org.example.factory.ComponentResolver;
import org.example.factory.DefaultComponentFactory;
import org.example.factory.NameBasedComponentResolver;
import org.example.factory.TypeBasedComponentResolver;
import org.example.naming.AnnotatedConstructorParameterQualifierResolver;
import org.example.naming.AnnotationBasedQualifierResolver;
import org.example.naming.ClassBasedQualifierResolver;
import org.example.naming.ConstructorParameterQualifierResolver;
import org.example.naming.QualifierResolver;
import org.example.naming.QualifierValidator;
import org.example.registry.ImmutableInstanceFactory;
import org.example.registry.SimpleRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Set;

public class ExternalJarInjectionTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        final Set<Class<?>> scannedComponents = Set.of();
        final QualifierResolver<Class<?>> qualifierResolver = new AnnotationBasedQualifierResolver(
            new ClassBasedQualifierResolver(),
            new QualifierValidator());
        final QualifierResolver<Parameter> constructorResolver = new AnnotatedConstructorParameterQualifierResolver(
            new ConstructorParameterQualifierResolver(qualifierResolver, scannedComponents));
        final ComponentResolver nameBasedComponentResolver = new NameBasedComponentResolver(
            qualifierResolver,
            new AssignableComponentResolver());

        applicationContext = new DefaultApplicationContext(
            scannedComponents,
            new SimpleRegistry(new HashMap<>(), new ImmutableInstanceFactory()),
            new DefaultComponentFactory(constructorResolver),
            new TypeBasedComponentResolver(nameBasedComponentResolver),
            qualifierResolver
        );
    }

    @Test
    void should() {
        // Missing requirement TODO external jar using this library.
    }
}
