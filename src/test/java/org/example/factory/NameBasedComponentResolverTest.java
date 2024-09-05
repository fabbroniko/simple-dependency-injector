package org.example.factory;

import org.example.naming.QualifierResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NameBasedComponentResolverTest {

    private static final String QUALIFYING_NAME = "test-name";
    private static final String INVALID_QUALIFYING_NAME = "invalid-test-name";

    @Mock
    private QualifierResolver<Class<?>> nameResolver;
    @Mock
    private ComponentResolver fallbackComponentResolver;
    @InjectMocks
    private NameBasedComponentResolver componentResolver;

    private final Set<Class<?>> scannedComponents = Set.of(Integer.class, String.class, Double.class);

    @Test
    void shouldResolveName() {
        when(nameResolver.resolve(any())).thenReturn(QUALIFYING_NAME);

        componentResolver.resolve(scannedComponents, Integer.class, QUALIFYING_NAME);

        verify(nameResolver).resolve(Integer.class);
    }

    @Test
    void shouldReturnMatchingValue() {
        when(nameResolver.resolve(any())).thenReturn(QUALIFYING_NAME);

        assertThat(componentResolver.resolve(scannedComponents, Integer.class, QUALIFYING_NAME)).isEqualTo(Integer.class);

        verify(nameResolver).resolve(Integer.class);
    }

    @Test
    void shouldDelegateWhenNoMatchIsFound() {
        when(nameResolver.resolve(any())).thenReturn(INVALID_QUALIFYING_NAME);

        componentResolver.resolve(scannedComponents, Integer.class, QUALIFYING_NAME);

        verify(fallbackComponentResolver).resolve(scannedComponents, Integer.class, QUALIFYING_NAME);
    }

    @Test
    void shouldReturnValueFromDelegate() {
        when(nameResolver.resolve(any())).thenReturn(INVALID_QUALIFYING_NAME);
        doReturn(Integer.class).when(fallbackComponentResolver).resolve(any(), any(), anyString());

        componentResolver.resolve(scannedComponents, Integer.class, QUALIFYING_NAME);

        verify(fallbackComponentResolver).resolve(scannedComponents, Integer.class, QUALIFYING_NAME);
    }
}