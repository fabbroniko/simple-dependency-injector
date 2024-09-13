package com.fabbroniko.sdi;

import com.fabbroniko.sdi.context.ApplicationContext;
import com.fabbroniko.sdi.context.DefaultApplicationContext;
import com.fabbroniko.sdi.factory.AssignableComponentResolver;
import com.fabbroniko.sdi.factory.ComponentResolver;
import com.fabbroniko.sdi.factory.DefaultComponentFactory;
import com.fabbroniko.sdi.factory.NameBasedComponentResolver;
import com.fabbroniko.sdi.factory.TypeBasedComponentResolver;
import com.fabbroniko.sdi.naming.AnnotatedConstructorParameterQualifierResolver;
import com.fabbroniko.sdi.naming.AnnotationBasedQualifierResolver;
import com.fabbroniko.sdi.naming.ClassBasedQualifierResolver;
import com.fabbroniko.sdi.naming.ConstructorParameterQualifierResolver;
import com.fabbroniko.sdi.naming.QualifierResolver;
import com.fabbroniko.sdi.naming.QualifierValidator;
import com.fabbroniko.sdi.registry.ImmutableInstanceFactory;
import com.fabbroniko.sdi.registry.SimpleRegistry;
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
