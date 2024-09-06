package org.example;

import org.example.annotation.Component;
import org.example.annotation.Configuration;
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
import org.example.scan.AnnotationPresentPredicate;
import org.example.scan.AnnotationScanner;
import org.example.scan.ClassScanner;
import org.example.scan.ClasspathClassScanner;
import org.example.scan.ContentFactory;
import org.example.scan.ContentSelector;
import org.example.scan.DefaultContentFactory;
import org.example.scan.DirectoryAndJarContentSelector;
import org.example.scan.FileFactory;
import org.example.scan.GenericAnnotationScanner;
import org.example.scan.JarResourceLocator;
import org.example.scan.StringToUrlResourceLocator;
import org.example.scan.SystemClassLoaderResourceLocator;
import org.example.scan.URIFileFactory;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Set;

public class DependencyInjector {

    public static ApplicationContext run(final Class<?> configuration) {
        final String rootPackage = configuration.getAnnotation(Configuration.class).componentScan();
        final FileFactory fileFactory = new URIFileFactory();
        final ContentFactory contentFactory = new DefaultContentFactory(fileFactory, new JarResourceLocator(new StringToUrlResourceLocator()));
        final ContentSelector contentSelector = new DirectoryAndJarContentSelector(contentFactory);
        final ClassScanner classScanner = new ClasspathClassScanner(contentSelector, new SystemClassLoaderResourceLocator());
        final AnnotationScanner annotationScanner = new GenericAnnotationScanner(classScanner, new AnnotationPresentPredicate(Component.class));
        final Set<Class<?>> scannedComponents = annotationScanner.getAnnotatedClasses(rootPackage);
        final QualifierResolver<Class<?>> qualifierResolver = new AnnotationBasedQualifierResolver(new ClassBasedQualifierResolver(), new QualifierValidator());
        final QualifierResolver<Parameter> constructorResolver = new AnnotatedConstructorParameterQualifierResolver(new ConstructorParameterQualifierResolver(qualifierResolver, scannedComponents));
        final ComponentResolver nameBasedComponentResolver = new NameBasedComponentResolver(qualifierResolver, new AssignableComponentResolver());

        return new DefaultApplicationContext(
            scannedComponents,
            new SimpleRegistry(new HashMap<>(), new ImmutableInstanceFactory()),
            new DefaultComponentFactory(constructorResolver),
            new TypeBasedComponentResolver(nameBasedComponentResolver),
            qualifierResolver
        );
    }
}