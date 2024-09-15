package com.fabbroniko.sdi;

import com.fabbroniko.sdi.scan.ClassLoaderWrapper;
import com.fabbroniko.sdi.scan.CustomClassLoaderWrapper;
import com.fabbroniko.sdi.scan.DefaultJarFileFactory;
import com.fabbroniko.sdi.scan.JarContent;
import com.fabbroniko.sdi.scan.JarResourceLocator;
import com.fabbroniko.sdi.scan.StringToUrlResourceLocator;
import com.fabbroniko.sdi.scan.URIFileFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class JarContentTest {

    private Set<Class<?>> scannedComponents;

    @BeforeEach
    void setUp() {
        final URL target = requireNonNull(Thread.currentThread().getContextClassLoader().getResource("scannable-jar.jar"));
        final URLClassLoader classLoader = new URLClassLoader(new URL[]{target}, this.getClass().getClassLoader());
        final ClassLoaderWrapper classLoaderWrapper = new CustomClassLoaderWrapper(classLoader);
        final JarContent jarContent = new JarContent(
            new URIFileFactory(),
            new JarResourceLocator(new StringToUrlResourceLocator()),
            classLoaderWrapper,
            new DefaultJarFileFactory());

        scannedComponents = jarContent.getClasses("com.fabbroniko.scannable", target);
    }

    @Test
    void shouldReturnClassesInJar() {
        assertThat(scannedComponents)
            .hasSize(1)
            .anySatisfy(clazz -> assertThat(clazz.getName()).isEqualTo("com.fabbroniko.scannable.Sample"));
    }
}
