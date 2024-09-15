package com.fabbroniko.sdi.scan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static java.util.Collections.enumeration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JarContentTest {

    private final String PACKAGE = "com.fabbroniko.sdi";
    private final String JAR_PATH = "/tmp/test.jar";

    @Mock
    private FileFactory fileFactory;
    @Mock
    private ResourceLocator resourceLocator;
    @Mock
    private ClassLoaderWrapper classLoaderWrapper;
    @Mock
    private JarFileFactory jarFileFactory;
    @Mock
    private File file;
    @Mock
    private JarFile jarFile;
    @Mock
    private JarEntry jarEntry;
    @Mock
    private URL url;
    @Mock
    private URL locatedUrl;
    @InjectMocks
    private JarContent jarContent;

    private final Class<?> returnedClass = Map.class;

    @BeforeEach
    void setUp() throws IOException {
        when(jarFileFactory.create(anyString())).thenReturn(jarFile);
        when(jarFile.entries()).thenReturn(enumeration(List.of(jarEntry)));
        when(file.getAbsolutePath()).thenReturn(JAR_PATH);
        when(jarEntry.getName()).thenReturn("com/fabbroniko/sdi/Test.class");
        doReturn(returnedClass).when(classLoaderWrapper).forName(anyString());
    }

    @Test
    void shouldCreateJarFile() throws IOException {
        jarContent.getClasses(PACKAGE, file);

        verify(jarFileFactory).create(JAR_PATH);
    }

    @Test
    void shouldLoadClass() {
        jarContent.getClasses(PACKAGE, file);

        verify(classLoaderWrapper).forName("com.fabbroniko.sdi.Test");
    }

    @Test
    void shouldCloseJarFile() throws IOException {
        jarContent.getClasses(PACKAGE, file);

        verify(jarFile).close();
    }

    @Test
    void shouldReturnSetWithValueFromClassLoader() {
        assertThat(jarContent.getClasses(PACKAGE, file))
            .containsExactly(returnedClass);
    }

    @Test
    void shouldLocateJar() {
        when(url.toString()).thenReturn(JAR_PATH);
        when(resourceLocator.locate(anyString())).thenReturn(locatedUrl);
        when(fileFactory.create(any())).thenReturn(file);

        jarContent.getClasses(PACKAGE, url);

        verify(resourceLocator).locate(JAR_PATH);
    }

    @Test
    void shouldCreateFileReferenceToJar() {
        when(url.toString()).thenReturn(JAR_PATH);
        when(resourceLocator.locate(anyString())).thenReturn(locatedUrl);
        when(fileFactory.create(any())).thenReturn(file);

        jarContent.getClasses(PACKAGE, url);

        verify(fileFactory).create(locatedUrl);
    }

    @Test
    void shouldNotLoadClassOutsidePackage() {
        when(jarEntry.getName()).thenReturn("com/fabbroniko/invalid/Test.class");
        reset(classLoaderWrapper);

        jarContent.getClasses(PACKAGE, file);

        verify(classLoaderWrapper, never()).forName(anyString());
    }
}