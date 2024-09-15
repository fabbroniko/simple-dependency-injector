package com.fabbroniko.sdi.scan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClasspathClassScannerTest {

    private static final String PACKAGE = "com.example";

    @Mock
    private ContentSelector contentSelector;
    @Mock
    private ResourceLocator resourceLocator;
    @Mock
    private URL url;
    @Mock
    private FileSystemContent fileSystemContent;
    @Mock
    private Set<Class<?>> scannedClasses;
    @InjectMocks
    private ClasspathClassScanner classScanner;

    @BeforeEach
    void setUp() {
        when(resourceLocator.locate(anyString())).thenReturn(url);
        when(contentSelector.select(any())).thenReturn(fileSystemContent);
        when(fileSystemContent.getClasses(anyString(), any(URL.class))).thenReturn(scannedClasses);
    }

    @Test
    void shouldLocateResource() {
        classScanner.get(PACKAGE);

        verify(resourceLocator).locate("com/example");
    }


    @Test
    void shouldSelectContentImplementation() {
        classScanner.get(PACKAGE);

        verify(contentSelector).select(url);
    }

    @Test
    void shouldGetClassesFromDirectoryContent() {
        classScanner.get(PACKAGE);

        verify(fileSystemContent).getClasses(PACKAGE, url);
    }

    @Test
    void shouldReturnClassesFromDirectoryContent() {
        assertThat(classScanner.get(PACKAGE))
            .isEqualTo(scannedClasses);
    }
}
