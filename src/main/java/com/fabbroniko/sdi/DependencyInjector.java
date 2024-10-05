package com.fabbroniko.sdi;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.annotation.Configuration;
import com.fabbroniko.sdi.context.AnnotationBasedStrategySelector;
import com.fabbroniko.sdi.context.ApplicationContext;
import com.fabbroniko.sdi.context.DefaultApplicationContext;
import com.fabbroniko.sdi.context.PrototypeStrategy;
import com.fabbroniko.sdi.context.SingletonStrategy;
import com.fabbroniko.sdi.context.Strategy;
import com.fabbroniko.sdi.context.StrategySelector;
import com.fabbroniko.sdi.factory.AssignableComponentResolver;
import com.fabbroniko.sdi.factory.ComponentFactory;
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
import com.fabbroniko.sdi.registry.Registry;
import com.fabbroniko.sdi.registry.SimpleRegistry;
import com.fabbroniko.sdi.scan.AnnotationPresentPredicate;
import com.fabbroniko.sdi.scan.AnnotationScanner;
import com.fabbroniko.sdi.scan.ClassScanner;
import com.fabbroniko.sdi.scan.ClasspathClassScanner;
import com.fabbroniko.sdi.scan.ContentFactory;
import com.fabbroniko.sdi.scan.ContentSelector;
import com.fabbroniko.sdi.scan.DefaultClassLoaderWrapper;
import com.fabbroniko.sdi.scan.DefaultContentFactory;
import com.fabbroniko.sdi.scan.DefaultJarFileFactory;
import com.fabbroniko.sdi.scan.DirectoryAndJarContentSelector;
import com.fabbroniko.sdi.scan.FileFactory;
import com.fabbroniko.sdi.scan.GenericAnnotationScanner;
import com.fabbroniko.sdi.scan.JarResourceLocator;
import com.fabbroniko.sdi.scan.ResourceLocator;
import com.fabbroniko.sdi.scan.StringToUrlResourceLocator;
import com.fabbroniko.sdi.scan.SystemClassLoaderResourceLocator;
import com.fabbroniko.sdi.scan.URIFileFactory;
import com.fabbroniko.ul.manager.LogManager;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Set;

public class DependencyInjector {

    public static ApplicationContext run(final Class<?> configuration) {
        final String rootPackage = configuration.getAnnotation(Configuration.class).componentScan();
        final FileFactory fileFactory = new URIFileFactory();
        final ResourceLocator jarResourceLocator = new JarResourceLocator(new StringToUrlResourceLocator());
        final ContentFactory contentFactory = new DefaultContentFactory(
            fileFactory,
            jarResourceLocator,
            new DefaultClassLoaderWrapper(),
            new DefaultJarFileFactory());
        final ContentSelector contentSelector = new DirectoryAndJarContentSelector(contentFactory);
        final ClassScanner classScanner = new ClasspathClassScanner(contentSelector, new SystemClassLoaderResourceLocator());
        final LogManager logManager = logManager(configuration);
        final AnnotationScanner annotationScanner = new GenericAnnotationScanner(classScanner, new AnnotationPresentPredicate(Component.class), logManager);
        final Set<Class<?>> scannedComponents = annotationScanner.getAnnotatedClasses(rootPackage);
        final QualifierResolver<Class<?>> qualifierResolver = new AnnotationBasedQualifierResolver(new ClassBasedQualifierResolver(), new QualifierValidator());
        final QualifierResolver<Parameter> constructorParamQualifierResolver = new ConstructorParameterQualifierResolver(qualifierResolver, scannedComponents);
        final QualifierResolver<Parameter> constructorResolver = new AnnotatedConstructorParameterQualifierResolver(constructorParamQualifierResolver);
        final ComponentResolver nameBasedComponentResolver = new NameBasedComponentResolver(qualifierResolver, new AssignableComponentResolver());

        final ComponentFactory componentFactory = new DefaultComponentFactory(constructorResolver, logManager);
        final Registry registry = new SimpleRegistry(new HashMap<>(), new ImmutableInstanceFactory());
        final Strategy singletonStrategy = new SingletonStrategy(
            logManager.getLogger(SingletonStrategy.class),
            componentFactory,
            registry);
        final Strategy prototypeStrategy = new PrototypeStrategy(
            logManager.getLogger(PrototypeStrategy.class),
            componentFactory);
        final StrategySelector strategySelector = new AnnotationBasedStrategySelector(singletonStrategy, prototypeStrategy);

        return new DefaultApplicationContext(
            logManager,
            scannedComponents,
            strategySelector,
            new TypeBasedComponentResolver(nameBasedComponentResolver),
            qualifierResolver
        );
    }

    private static LogManager logManager(final Class<?> configuration) {
        final Class<? extends LogManager> logger = configuration.getAnnotation(Configuration.class).logger();

        try {
            return logger.getConstructor().newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}