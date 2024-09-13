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
class DirectoryContentTest {

    private static final String PACKAGE = "com.fabbroniko.sdi";

    @Mock
    private ContentFactory contentFactory;
    @Mock
    private FileSystemContent subDirectoryContent;
    @Mock
    private FileSystemContent fileContent;
    @Mock
    private File directory;
    @Mock
    private File subDirectory;
    @Mock
    private File file;
    @Mock
    private URL url;
    @Mock
    private FileFactory fileFactory;
    @InjectMocks
    private DirectoryContent directoryContent;

    @BeforeEach
    void setUp() {
        when(subDirectory.getName()).thenReturn("scan");
        when(directory.listFiles()).thenReturn(new File[] {subDirectory, file});
        when(contentFactory.createDirectory()).thenReturn(subDirectoryContent);
        when(contentFactory.createFile()).thenReturn(fileContent);
        when(subDirectory.isDirectory()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        when(fileContent.getClasses(anyString(), any(File.class))).thenReturn(Set.of(Object.class));
        when(subDirectoryContent.getClasses(anyString(), any(File.class))).thenReturn(Set.of(Integer.class));
    }

    @Test
    void shouldCreateSubDirectoryContent() {
        directoryContent.getClasses(PACKAGE, directory);

        verify(contentFactory).createDirectory();
    }

    @Test
    void shouldGetClassesFromSubDirectoryContent() {
        directoryContent.getClasses(PACKAGE, directory);

        verify(subDirectoryContent).getClasses("com.fabbroniko.sdi.scan", subDirectory);
    }

    @Test
    void shouldGetClassesFromFileContent() {
        directoryContent.getClasses(PACKAGE, directory);

        verify(fileContent).getClasses(PACKAGE, file);
    }

    @Test
    void shouldReturnCombinedSets() {
        assertThat(directoryContent.getClasses(PACKAGE, directory))
            .containsExactlyInAnyOrder(Object.class, Integer.class);
    }

    @Test
    void shouldCreateFileFromUrl() {
        when(fileFactory.create(any())).thenReturn(directory);

        directoryContent.getClasses(PACKAGE, url);

        verify(fileFactory).create(url);
    }
}