package org.example.factory;

import org.example.exception.CircularDependencyException;
import org.example.exception.InvalidDependencyException;
import org.example.registry.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComponentFactoryImplTest {

    @Mock
    private ComponentResolver componentResolver;
    @Mock
    private Registry registry;
    @Mock
    private Set<Class<?>> scannedComponents;
    @InjectMocks
    private ComponentFactoryImpl beanFactory;

    @BeforeEach
    void setUp() {
        doReturn(Object.class).when(componentResolver).resolve(any(), any());
    }

    @Test
    void shouldResolveClassToInitialize() {
        when(scannedComponents.contains(any(Class.class))).thenReturn(true);

        beanFactory.create(registry, scannedComponents, Object.class);

        verify(componentResolver).resolve(scannedComponents, Object.class);
    }

    @Test
    void shouldThrowExceptionWhenTargetIsNotComponent() {
        when(scannedComponents.contains(any(Class.class))).thenReturn(false);

        assertThatThrownBy(() -> beanFactory.create(registry, scannedComponents, Object.class))
            .isInstanceOf(InvalidDependencyException.class);
    }

    @Test
    void shouldThrowExceptionWhenInstanceAlreadyProcessing() {
        when(scannedComponents.contains(any(Class.class))).thenReturn(true);
        when(registry.isProcessing(any())).thenReturn(true);

        assertThatThrownBy(() -> beanFactory.create(registry, scannedComponents, Object.class))
            .isInstanceOf(CircularDependencyException.class);
    }

    @Test
    void shouldSetClassAsProcessing() {
        when(scannedComponents.contains(any(Class.class))).thenReturn(true);

        beanFactory.create(registry, scannedComponents, Object.class);

        verify(registry).process(Object.class);
    }
}