package org.example;

import org.example.scan.ContentFactory;
import org.example.scan.ContentSelector;
import org.example.scan.DefaultContentFactory;
import org.example.scan.ClassScanner;
import org.example.scan.ClasspathClassScanner;
import org.example.scan.DirectoryAndJarContentSelector;
import org.example.scan.FileFactory;
import org.example.scan.JarResourceLocator;
import org.example.scan.StringToUrlResourceLocator;
import org.example.scan.SystemClassLoaderResourceLocator;
import org.example.scan.URIFileFactory;
import org.example.target.circular.FirstCircularDependency;
import org.example.target.circular.SecondCircularDependency;
import org.example.target.interfaced.DependsOnInterface;
import org.example.target.interfaced.InterfacedClass;
import org.example.target.interfaced.SampleInterface;
import org.example.target.nested.Root;
import org.example.target.nested.inner.Nested;
import org.example.target.util.NoDependenciesTestClass;
import org.example.target.util.WithDependencyTestClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ClasspathClassScannerTest {

    private Set<Class<?>> scannedClasses;

    @BeforeEach
    void setUp() {
        final FileFactory fileFactory = new URIFileFactory();
        final ContentFactory contentFactory = new DefaultContentFactory(fileFactory, new JarResourceLocator(new StringToUrlResourceLocator()));
        final ContentSelector contentSelector = new DirectoryAndJarContentSelector(contentFactory);
        final ClassScanner classScanner = new ClasspathClassScanner(contentSelector, new SystemClassLoaderResourceLocator());

        scannedClasses = classScanner.get("org.example.target");
    }

    @ParameterizedTest
    @MethodSource("targetClasses")
    void shouldFindAllTargetClasses(final Class<?> clazz) {
        assertThat(scannedClasses).contains(clazz);
    }

    private static Stream<Arguments> targetClasses() {
        return Stream.of(
            Arguments.of(FirstCircularDependency.class),
            Arguments.of(SecondCircularDependency.class),
            Arguments.of(DependsOnInterface.class),
            Arguments.of(InterfacedClass.class),
            Arguments.of(SampleInterface.class),
            Arguments.of(Root.class),
            Arguments.of(Nested.class),
            Arguments.of(NoDependenciesTestClass.class),
            Arguments.of(WithDependencyTestClass.class)
        );
    }
}
