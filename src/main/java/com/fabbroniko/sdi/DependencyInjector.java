package com.fabbroniko.sdi;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;
import com.fabbroniko.sdi.context.ApplicationContext;
import com.fabbroniko.sdi.context.DefaultApplicationContext;
import com.fabbroniko.sdi.factory.AssignableComponentResolver;
import com.fabbroniko.sdi.factory.ComponentResolver;
import com.fabbroniko.sdi.factory.NameBasedComponentResolver;
import com.fabbroniko.sdi.factory.TypeBasedComponentResolver;
import com.fabbroniko.sdi.factory.DefaultComponentFactory;
import com.fabbroniko.sdi.naming.AnnotatedConstructorParameterQualifierResolver;
import com.fabbroniko.sdi.naming.AnnotationBasedQualifierResolver;
import com.fabbroniko.sdi.naming.ClassBasedQualifierResolver;
import com.fabbroniko.sdi.naming.ConstructorParameterQualifierResolver;
import com.fabbroniko.sdi.naming.QualifierResolver;
import com.fabbroniko.sdi.naming.QualifierValidator;
import com.fabbroniko.sdi.registry.SimpleRegistry;
import com.fabbroniko.sdi.registry.ImmutableInstanceFactory;
import com.fabbroniko.sdi.scan.AnnotationPresentPredicate;
import com.fabbroniko.sdi.scan.AnnotationScanner;
import com.fabbroniko.sdi.scan.ClassScanner;
import com.fabbroniko.sdi.scan.ClasspathClassScanner;
import com.fabbroniko.sdi.scan.DefaultContentFactory;
import com.fabbroniko.sdi.scan.GenericAnnotationScanner;
import com.fabbroniko.sdi.scan.SystemClassLoaderResourceLocator;
import com.fabbroniko.sdi.scan.URIFileFactory;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Set;

public class DependencyInjector {

    public static ApplicationContext run(final Class<?> configuration) {
        final String rootPackage = configuration.getAnnotation(Configuration.class).componentScan();
        final ClassScanner classScanner = new ClasspathClassScanner(new DefaultContentFactory(), new SystemClassLoaderResourceLocator(), new URIFileFactory());
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