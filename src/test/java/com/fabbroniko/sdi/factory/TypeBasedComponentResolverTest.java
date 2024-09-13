package com.fabbroniko.sdi.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TypeBasedComponentResolverTest {

    private static final String QUALIFYING_NAME = "test-name";

    @Mock
    private ComponentResolver fallbackComponentResolver;
    @Mock
    private Set<Class<?>> scannedComponents;
    @InjectMocks
    private TypeBasedComponentResolver componentResolver;

    @Test
    void shouldReturnTargetClassIfNotInterface() {
        assertThat(componentResolver.resolve(scannedComponents, Integer.class, QUALIFYING_NAME))
            .isEqualTo(Integer.class);
    }

    @Test
    void shouldDelegateResolutionWhenTargetIsInterface() {
        componentResolver.resolve(scannedComponents, Map.class, QUALIFYING_NAME);

        verify(fallbackComponentResolver).resolve(scannedComponents, Map.class, QUALIFYING_NAME);
    }

    @Test
    void shouldReturnValueFromDelegateWhenTargetIsInterface() {
        doReturn(HashMap.class).when(fallbackComponentResolver).resolve(any(), any(), anyString());

        assertThat(componentResolver.resolve(scannedComponents, Map.class, QUALIFYING_NAME))
            .isEqualTo(HashMap.class);
    }
}