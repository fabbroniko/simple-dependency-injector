package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.exception.CircularDependencyException;
import com.fabbroniko.sdi.factory.ComponentFactory;
import com.fabbroniko.sdi.registry.Registry;
import com.fabbroniko.ul.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingletonStrategyTest {

    @Mock
    private Logger logger;
    @Mock
    private ComponentFactory componentFactory;
    @Mock
    private Registry registry;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private Object instance;
    @InjectMocks
    private SingletonStrategy singletonStrategy;

    @Test
    void shouldCheckTargetIsProcessing() {
        singletonStrategy.getInstance(Object.class, applicationContext);

        verify(registry).isProcessing(Object.class);
    }

    @Test
    void shouldThrowExceptionWhenAlreadyProcessing() {
        when(registry.isProcessing(any())).thenReturn(true);

        assertThatThrownBy(() -> singletonStrategy.getInstance(Object.class, applicationContext))
            .isInstanceOf(CircularDependencyException.class)
            .hasMessage("Component java.lang.Object is already processing.");

        verify(logger).fatal("circular_dependency", "java.lang.Object");
    }

    @Test
    void shouldGetInstanceFromRegistry() {
        singletonStrategy.getInstance(Object.class, applicationContext);

        verify(registry).getInstance(Object.class);
    }

    @Test
    void shouldLogWhenInstanceAlreadyInRegistry() {
        when(registry.getInstance(any())).thenReturn(Optional.of(instance));

        singletonStrategy.getInstance(Object.class, applicationContext);

        verify(logger).trace("target_found_in_registry", "java.lang.Object");
    }

    @Test
    void shouldReturnInstanceFormRegistry() {
        when(registry.getInstance(any())).thenReturn(Optional.of(instance));

        assertThat(singletonStrategy.getInstance(Object.class, applicationContext))
            .isEqualTo(instance);
    }

    @Test
    void shouldMarkClassAsProcessing() {
        when(registry.getInstance(any())).thenReturn(Optional.empty());

        singletonStrategy.getInstance(Object.class, applicationContext);

        verify(registry).process(Object.class);
    }

    @Test
    void shouldLogCreateNewInstance() {
        when(registry.getInstance(any())).thenReturn(Optional.empty());

        singletonStrategy.getInstance(Object.class, applicationContext);

        verify(logger).trace("create_new_instance", "java.lang.Object");
    }

    @Test
    void shouldCreateInstanceOfTarget() {
        when(registry.getInstance(any())).thenReturn(Optional.empty());

        singletonStrategy.getInstance(Object.class, applicationContext);

        verify(componentFactory).create(Object.class, applicationContext);
    }

    @Test
    void shouldInsertCreatedInstanceInRegistry() {
        when(registry.getInstance(any())).thenReturn(Optional.empty());
        when(componentFactory.create(any(), any())).thenReturn(instance);

        singletonStrategy.getInstance(Object.class, applicationContext);

        verify(registry).insert(Object.class, instance);
    }

    @Test
    void shouldReturnCreatedInstance() {
        when(registry.getInstance(any())).thenReturn(Optional.empty());
        when(componentFactory.create(any(), any())).thenReturn(instance);

        assertThat(singletonStrategy.getInstance(Object.class, applicationContext))
            .isEqualTo(instance);
    }
}