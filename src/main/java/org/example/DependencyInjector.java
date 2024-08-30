package org.example;

import org.example.annotation.Component;
import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.context.ApplicationContextImpl;
import org.example.factory.AssignableComponentResolver;
import org.example.factory.ComponentFactory;
import org.example.factory.ComponentFactoryImpl;
import org.example.factory.ComponentFactoryValidator;
import org.example.registry.Registry;
import org.example.registry.RegistryFactory;
import org.example.registry.AssignableRegistryFactory;
import org.example.scan.AnnotationPresentPredicate;
import org.example.scan.AnnotationScanner;
import org.example.scan.ClassScanner;
import org.example.scan.ClasspathClassScanner;
import org.example.scan.ContentFactoryImpl;
import org.example.scan.GenericAnnotationScanner;
import org.example.scan.SystemClassLoaderResourceLocator;
import org.example.scan.URIFileFactory;

import java.util.Set;

public class DependencyInjector {

    public static ApplicationContext run(final Class<?> configuration) {
        final String rootPackage = configuration.getAnnotation(Configuration.class).componentScan();
        final ClassScanner classScanner = new ClasspathClassScanner(new ContentFactoryImpl(), new SystemClassLoaderResourceLocator(), new URIFileFactory());
        final AnnotationScanner annotationScanner = new GenericAnnotationScanner(classScanner, new AnnotationPresentPredicate(Component.class));
        final Set<Class<?>> annotatedClasses = annotationScanner.getAnnotatedClasses(rootPackage);
        final RegistryFactory registryFactory = new AssignableRegistryFactory();
        final Registry registry = registryFactory.create();
        final ComponentFactory componentFactory = new ComponentFactoryImpl(new AssignableComponentResolver(), registry, annotatedClasses, new ComponentFactoryValidator());

        return new ApplicationContextImpl(registry, componentFactory);
    }
}
