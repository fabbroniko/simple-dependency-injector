package com.fabbroniko.sdi.target;

import com.fabbroniko.sdi.scan.DefaultContentFactory;
import com.fabbroniko.sdi.scan.ClassScanner;
import com.fabbroniko.sdi.scan.ClasspathClassScanner;
import com.fabbroniko.sdi.scan.SystemClassLoaderResourceLocator;
import com.fabbroniko.sdi.scan.URIFileFactory;
import com.fabbroniko.sdi.target.circular.FirstCircularDependency;
import com.fabbroniko.sdi.target.circular.SecondCircularDependency;
import com.fabbroniko.sdi.target.interfaced.DependsOnInterface;
import com.fabbroniko.sdi.target.interfaced.InterfacedClass;
import com.fabbroniko.sdi.target.interfaced.SampleInterface;
import com.fabbroniko.sdi.target.nested.Root;
import com.fabbroniko.sdi.target.nested.inner.Nested;
import com.fabbroniko.sdi.target.util.NoDependenciesTestClass;
import com.fabbroniko.sdi.target.util.WithDependencyTestClass;
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
        final ClassScanner classScanner = new ClasspathClassScanner(new DefaultContentFactory(), new SystemClassLoaderResourceLocator(), new URIFileFactory());
        scannedClasses = classScanner.get("com.fabbroniko.sdi.target");
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
