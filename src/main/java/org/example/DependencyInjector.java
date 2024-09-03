package org.example;

import org.example.annotation.Component;
import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.context.ApplicationContextImpl;
import org.example.factory.AssignableComponentResolver;
import org.example.factory.ComponentFactoryImpl;
import org.example.naming.AnnotatedConstructorParameterQualifierResolver;
import org.example.naming.AnnotationBasedQualifierResolver;
import org.example.naming.ClassBasedQualifierResolver;
import org.example.naming.QualifierResolver;
import org.example.naming.QualifierValidator;
import org.example.registry.SimpleRegistry;
import org.example.registry.ImmutableInstanceFactory;
import org.example.scan.AnnotationPresentPredicate;
import org.example.scan.AnnotationScanner;
import org.example.scan.ClassScanner;
import org.example.scan.ClasspathClassScanner;
import org.example.scan.ContentFactoryImpl;
import org.example.scan.GenericAnnotationScanner;
import org.example.scan.SystemClassLoaderResourceLocator;
import org.example.scan.URIFileFactory;

import java.util.HashMap;
import java.util.Set;

public class DependencyInjector {

    public static ApplicationContext run(final Class<?> configuration) {
        final String rootPackage = configuration.getAnnotation(Configuration.class).componentScan();
        final ClassScanner classScanner = new ClasspathClassScanner(new ContentFactoryImpl(), new SystemClassLoaderResourceLocator(), new URIFileFactory());
        final AnnotationScanner annotationScanner = new GenericAnnotationScanner(classScanner, new AnnotationPresentPredicate(Component.class));
        final Set<Class<?>> scannedComponents = annotationScanner.getAnnotatedClasses(rootPackage);
        final QualifierResolver<Class<?>> qualifierResolver = new AnnotationBasedQualifierResolver(new ClassBasedQualifierResolver(), new QualifierValidator());

        return new ApplicationContextImpl(
            new SimpleRegistry(new HashMap<>(), new ImmutableInstanceFactory()),
            new ComponentFactoryImpl(new AnnotatedConstructorParameterQualifierResolver(qualifierResolver, scannedComponents)),
            new AssignableComponentResolver(scannedComponents, qualifierResolver),
            qualifierResolver
        );
    }
}