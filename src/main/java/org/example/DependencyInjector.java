package org.example;

import org.example.annotation.Component;
import org.example.annotation.Configuration;
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

    private static Registry registry;

    public static void run(final Class<?> configuration) {
        final String rootPackage = configuration.getAnnotation(Configuration.class).componentScan();
        final ClassScanner classScanner = new ClasspathClassScanner(new ContentFactoryImpl(), new SystemClassLoaderResourceLocator(), new URIFileFactory());
        final AnnotationScanner annotationScanner = new GenericAnnotationScanner(classScanner, new AnnotationPresentPredicate(Component.class));
        final Set<Class<?>> annotatedClasses = annotationScanner.getAnnotatedClasses(rootPackage);
        final BeanFactory beanFactory = new BeanFactoryImpl();
        final RegistryFactory registryFactory = new AssignableRegistryFactory();
        registry = registryFactory.create();

        annotatedClasses.forEach(clazz -> {
            if(registry.getInstance(clazz).isEmpty()) {
                beanFactory.create(registry, annotatedClasses, clazz);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(final Class<T> c) {
        return (T) registry.getInstance(c).orElseThrow();
    }
}
