package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.exception.CircularDependencyException;
import com.fabbroniko.sdi.factory.ComponentFactory;
import com.fabbroniko.sdi.factory.ComponentResolver;
import com.fabbroniko.sdi.naming.QualifierResolver;
import com.fabbroniko.sdi.registry.Registry;
import com.fabbroniko.ul.Logger;
import com.fabbroniko.ul.manager.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class DefaultApplicationContextTest {

    @Mock
    private Set<Class<?>> scannedComponents;
    @Mock
    private Registry registry;
    @Mock
    private ComponentFactory componentFactory;
    @Mock
    private ComponentResolver componentResolver;
    @Mock
    private QualifierResolver<Class<?>> nameResolver;
    @Mock
    private LogManager logManager;
    @Mock
    private Logger logger;
    @Mock
    private Object instance;

    private DefaultApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        when(logManager.getLogger(any())).thenReturn(logger);

        applicationContext = new DefaultApplicationContext(logManager,
            scannedComponents,
            registry,
            componentFactory,
            componentResolver,
            nameResolver);
    }

    @Test
    void shouldReturnSelfWhenTargetIsApplicationContext() {
        assertThat((Object) applicationContext.getInstance(ApplicationContext.class))
            .isEqualTo(applicationContext);
    }

    @Test
    void shouldThrowCircularDependencyException() {
        doReturn(Object.class).when(componentResolver).resolve(any(), any(), any());
        when(registry.isProcessing(any())).thenReturn(true);

        assertThatThrownBy(() -> applicationContext.getInstance(Integer.class))
            .isInstanceOf(CircularDependencyException.class);

        verify(logger).fatal("circular_dependency", "java.lang.Object");
    }

    @Nested
    class GetInstanceTest {

        @BeforeEach
        void setUp() {
            doReturn(Object.class).when(componentResolver).resolve(any(), any(), any());
            when(registry.isProcessing(any())).thenReturn(false);
            when(registry.getInstance(any())).thenReturn(Optional.of(instance));
        }

        @Test
        void shouldLogGetInstance() {
            applicationContext.getInstance(Object.class);

            verify(logger).trace("get_instance", "java.lang.Object", null);
        }

        @Test
        void shouldResolveName() {
            applicationContext.getInstance(Object.class);

            verify(nameResolver).resolve(Object.class);
        }

        @Test
        void shouldLogResolveTargetClass() {
            applicationContext.getInstance(Object.class);

            verify(logger).trace("resolve_target_class", "java.lang.Object", null, "java.lang.Object");
        }

        @Test
        void shouldResolveClassToInitialize() {
            when(nameResolver.resolve(any())).thenReturn("integer");

            applicationContext.getInstance(Integer.class);

            verify(componentResolver).resolve(scannedComponents, Integer.class, "integer");
        }

        @Test
        void shouldGetInstanceFromRegistry() {
            applicationContext.getInstance(Integer.class);

            verify(registry).getInstance(Object.class);
        }

        @Test
        void shouldLogTargetFoundInRegistry() {
            applicationContext.getInstance(Object.class);

            verify(logger).trace("target_found_in_registry", "java.lang.Object");
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
        void shouldRegisterTargetAsProcessing() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());

            applicationContext.getInstance(Integer.class);

            verify(registry).process(Object.class);
        }

        @Test
        void shouldLogCreateNewInstance() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());

            applicationContext.getInstance(Integer.class);

            verify(logger).trace("create_new_instance", "java.lang.Object");
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