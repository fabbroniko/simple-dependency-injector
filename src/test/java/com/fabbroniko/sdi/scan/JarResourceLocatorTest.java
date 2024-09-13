package com.fabbroniko.sdi.scan;

import org.example.scan.JarResourceLocator;
import org.example.scan.ResourceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JarResourceLocatorTest {

    private static final String VALID_INPUT = "jar:file:/Some/Place.jar!com/example/test";

    @Mock
    private ResourceLocator resourceLocator;
    @Mock
    private URL url;
    @InjectMocks
    private JarResourceLocator jarResourceLocator;

    @Test
    void shouldLocateSanitizedTarget() {
        jarResourceLocator.locate(VALID_INPUT);

        verify(resourceLocator).locate("file:/Some/Place.jar");
    }

    @Test
    void shouldReturnURLFromResourceLocator() {
        when(resourceLocator.locate(anyString())).thenReturn(url);

        assertThat(jarResourceLocator.locate(VALID_INPUT))
            .isEqualTo(url);


    }
}