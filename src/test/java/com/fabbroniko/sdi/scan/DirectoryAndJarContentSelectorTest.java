package com.fabbroniko.sdi.scan;

import org.example.scan.ContentFactory;
import org.example.scan.DirectoryAndJarContentSelector;
import org.example.scan.FileSystemContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectoryAndJarContentSelectorTest {

    private static final String JAR_PROTOCOL = "jar";
    private static final String DIRECTORY_PROTOCOL = "file";

    @Mock
    private ContentFactory contentFactory;
    @Mock
    private URL url;
    @Mock
    private FileSystemContent fileSystemContent;
    @InjectMocks
    private DirectoryAndJarContentSelector directoryAndJarContentSelector;

    @Test
    void shouldCreateJarContentWhenProtocolIsJar() {
        when(url.getProtocol()).thenReturn(JAR_PROTOCOL);

        directoryAndJarContentSelector.select(url);

        verify(contentFactory).createJar();
    }

    @Test
    void shouldReturnJarContentWhenProtocolIsJar() {
        when(url.getProtocol()).thenReturn(JAR_PROTOCOL);
        when(contentFactory.createJar()).thenReturn(fileSystemContent);

        assertThat(directoryAndJarContentSelector.select(url))
            .isEqualTo(fileSystemContent);
    }

    @Test
    void shouldCreateDirectoryContentWhenProtocolIsFile() {
        when(url.getProtocol()).thenReturn(DIRECTORY_PROTOCOL);

        directoryAndJarContentSelector.select(url);

        verify(contentFactory).createDirectory();
    }

    @Test
    void shouldReturnDirectoryContentWhenProtocolIsFile() {
        when(url.getProtocol()).thenReturn(DIRECTORY_PROTOCOL);
        when(contentFactory.createDirectory()).thenReturn(fileSystemContent);

        assertThat(directoryAndJarContentSelector.select(url))
            .isEqualTo(fileSystemContent);
    }
}