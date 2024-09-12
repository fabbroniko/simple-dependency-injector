package com.fabbroniko.sdi.scan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileContentTest {

    private static final String PACKAGE = "java.lang";
    private static final String INVALID_FILE_NAME = "invalid.png";
    private static final String VALID_FILE_NAME = "Object.class";

    @Mock
    private File file;
    @Mock
    private ClassLoaderWrapper classLoaderWrapper;
    @InjectMocks
    private FileContent fileDirectoryContent;

    @Test
    void shouldReturnEmptySetIfNotAClassFile() {
        when(file.getName()).thenReturn(INVALID_FILE_NAME);

        assertThat(fileDirectoryContent.getClasses(PACKAGE, file))
            .isEmpty();
    }

    @Test
    void shouldDelegateClassLoading() {
        when(file.getName()).thenReturn(VALID_FILE_NAME);
        doReturn(Object.class).when(classLoaderWrapper).forName(anyString());

        fileDirectoryContent.getClasses(PACKAGE, file);

        verify(classLoaderWrapper).forName("java.lang.Object");
    }

    @Test
    void shouldReturnValueFromClassLoaderAsSet() {
        when(file.getName()).thenReturn(VALID_FILE_NAME);
        doReturn(Object.class).when(classLoaderWrapper).forName(anyString());

        assertThat(fileDirectoryContent.getClasses(PACKAGE, file))
            .containsExactly(Object.class);
    }
}