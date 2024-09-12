package com.fabbroniko.sdi.scan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
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
    private ContentFactory contentFactory;
    @Mock
    private ResourceLocator resourceLocator;
    @Mock
    private FileFactory fileFactory;
    @Mock
    private File file;
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
        when(fileFactory.create(any())).thenReturn(file);
        when(contentFactory.createDirectory()).thenReturn(fileSystemContent);
        when(fileSystemContent.getClasses(anyString(), any())).thenReturn(scannedClasses);
    }

    @Test
    void shouldLocateResource() {
        classScanner.get(PACKAGE);

        verify(resourceLocator).locate("com/example");
    }

    @Test
    void shouldCreateFileFromResourceURL() {
        classScanner.get(PACKAGE);

        verify(fileFactory).create(url);
    }

    @Test
    void shouldCreateRootDirectoryContent() {
        classScanner.get(PACKAGE);

        verify(contentFactory).createDirectory();
    }

    @Test
    void shouldGetClassesFromDirectoryContent() {
        classScanner.get(PACKAGE);

        verify(fileSystemContent).getClasses(PACKAGE, file);
    }

    @Test
    void shouldReturnClassesFromDirectoryContent() {
        assertThat(classScanner.get(PACKAGE))
            .isEqualTo(scannedClasses);
    }
}
