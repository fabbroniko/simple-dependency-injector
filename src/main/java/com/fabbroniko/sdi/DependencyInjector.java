package com.fabbroniko.sdi;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;
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