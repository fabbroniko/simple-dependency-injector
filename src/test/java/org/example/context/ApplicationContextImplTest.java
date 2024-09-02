package org.example.context;

import org.example.exception.CircularDependencyException;
import org.example.factory.ComponentFactory;
import org.example.factory.ComponentResolver;
import org.example.naming.QualifyingNameResolver;
import org.example.registry.Registry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationContextImplTest {

    @Mock
    private Registry registry;
    @Mock
    private ComponentFactory componentFactory;
    @Mock
    private ComponentResolver componentResolver;
    @Mock
    private QualifyingNameResolver nameResolver;
    @Mock
    private Set<Class<?>> scannedComponents;
    @Mock
    private Object instance;
    @InjectMocks
    private ApplicationContextImpl applicationContext;

    @Test
    void shouldReturnSelfWhenTargetIsApplicationContext() {
        assertThat((Object) applicationContext.getInstance(ApplicationContext.class))
            .isEqualTo(applicationContext);
    }

    @Test
    void shouldThrowCircularDependencyException() {
        doReturn(Object.class).when(componentResolver).resolve(any(), any());
        when(registry.isProcessing(any())).thenReturn(true);

        assertThatThrownBy(() -> applicationContext.getInstance(Integer.class))
            .isInstanceOf(CircularDependencyException.class);
    }

    @Nested
    class GetInstanceTest {

        @BeforeEach
        void setUp() {
            doReturn(Object.class).when(componentResolver).resolve(any(), any());
            when(registry.isProcessing(any())).thenReturn(false);
            when(registry.getInstance(any())).thenReturn(Optional.of(instance));
        }

        @Test
        void shouldResolveName() {
            applicationContext.getInstance(Object.class);

            verify(nameResolver).resolveFor(Object.class);
        }
        @Test
        void shouldResolveClassToInitialize() {
            when(nameResolver.resolveFor(any())).thenReturn("integer");

            applicationContext.getInstance(Integer.class);

            verify(componentResolver).resolve(Integer.class, "integer");
        }

        @Test
        void shouldRegisterTargetAsProcessing() {
            applicationContext.getInstance(Integer.class);

            verify(registry).process(Object.class);
        }

        @Test
        void shouldGetInstanceFromRegistry() {
            applicationContext.getInstance(Integer.class);

            verify(registry).getInstance(Object.class);
        }

        @Test
        void shouldNotCreateFromFactory() {
            applicationContext.getInstance(Integer.class);

            verify(componentFactory, never()).create(any(), any());
        }

        @Test
        void shouldReturnValue() {
            assertThat((Object) applicationContext.getInstance(Integer.class))
                .isEqualTo(instance);
        }

        @Test
        void shouldCreateComponent() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());

            applicationContext.getInstance(Integer.class);

            verify(componentFactory).create(Object.class, applicationContext);
        }

        @Test
        void shouldRegisterNewInstance() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());
            when(componentFactory.create(any(), any())).thenReturn(instance);

            applicationContext.getInstance(Integer.class);

            verify(registry).insert(Object.class, instance);
        }

        @Test
        void shouldReturnNewInstance() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());
            when(componentFactory.create(any(), any())).thenReturn(instance);

            assertThat((Object) applicationContext.getInstance(Integer.class))
                .isEqualTo(instance);
        }
    }
}