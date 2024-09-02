package org.example;

import org.example.annotation.Component;
import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.context.ApplicationContextImpl;
import org.example.factory.AssignableComponentResolver;
import org.example.factory.ComponentFactoryImpl;
import org.example.naming.AnnotationBasedConstructorParameterNameResolver;
import org.example.naming.AnnotationBasedNameResolver;
import org.example.naming.QualifyingNameResolver;
import org.example.registry.AssignableRegistry;
import org.example.registry.InstanceStoreFactory;
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
        final QualifyingNameResolver qualifyingNameResolver = new AnnotationBasedNameResolver();

        return new ApplicationContextImpl(
            new AssignableRegistry(new HashMap<>(), new InstanceStoreFactory()),
            new ComponentFactoryImpl(new AnnotationBasedConstructorParameterNameResolver(qualifyingNameResolver, scannedComponents)),
            new AssignableComponentResolver(scannedComponents, qualifyingNameResolver),
            qualifyingNameResolver
        );
    }
}