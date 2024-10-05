package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.annotation.Component;
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
            doReturn(FirstTestClass.class).when(componentResolver).resolve(any(), any(), any());
            when(registry.isProcessing(any())).thenReturn(false);
            when(registry.getInstance(any())).thenReturn(Optional.of(instance));
        }

        @Test
        void shouldLogGetInstance() {
            applicationContext.getInstance(FirstTestClass.class);

            verify(logger).trace("get_instance", "com.fabbroniko.sdi.context.DefaultApplicationContextTest$FirstTestClass", null);
        }

        @Test
        void shouldResolveName() {
            applicationContext.getInstance(FirstTestClass.class);

            verify(nameResolver).resolve(FirstTestClass.class);
        }

        @Test
        void shouldLogResolveTargetClass() {
            applicationContext.getInstance(FirstTestClass.class);

            verify(logger).trace(
                "resolve_target_class",
                "com.fabbroniko.sdi.context.DefaultApplicationContextTest$FirstTestClass",
                null,
                "com.fabbroniko.sdi.context.DefaultApplicationContextTest$FirstTestClass");
        }

        @Test
        void shouldResolveClassToInitialize() {
            when(nameResolver.resolve(any())).thenReturn("integer");

            applicationContext.getInstance(FirstTestClass.class);

            verify(componentResolver).resolve(scannedComponents, FirstTestClass.class, "integer");
        }

        @Test
        void shouldGetInstanceFromRegistry() {
            applicationContext.getInstance(FirstTestClass.class);

            verify(registry).getInstance(FirstTestClass.class);
        }

        @Test
        void shouldLogTargetFoundInRegistry() {
            applicationContext.getInstance(FirstTestClass.class);

            verify(logger).trace("target_found_in_registry", "com.fabbroniko.sdi.context.DefaultApplicationContextTest$FirstTestClass");
        }

        @Test
        void shouldNotCreateFromFactory() {
            applicationContext.getInstance(Integer.class);

            verify(componentFactory, never()).create(any(), any());
        }

        @Test
        void shouldReturnValue() {
            assertThat((Object) applicationContext.getInstance(FirstTestClass.class))
                .isEqualTo(instance);
        }

        @Test
        void shouldRegisterTargetAsProcessing() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());

            applicationContext.getInstance(FirstTestClass.class);

            verify(registry).process(FirstTestClass.class);
        }

        @Test
        void shouldLogCreateNewInstance() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());

            applicationContext.getInstance(FirstTestClass.class);

            verify(logger).trace("create_new_instance", "com.fabbroniko.sdi.context.DefaultApplicationContextTest$FirstTestClass");
        }

        @Test
        void shouldCreateComponent() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());

            applicationContext.getInstance(FirstTestClass.class);

            verify(componentFactory).create(FirstTestClass.class, applicationContext);
        }

        @Test
        void shouldRegisterNewInstance() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());
            when(componentFactory.create(any(), any())).thenReturn(instance);

            applicationContext.getInstance(FirstTestClass.class);

            verify(registry).insert(FirstTestClass.class, instance);
        }

        @Test
        void shouldReturnNewInstance() {
            when(registry.getInstance(any())).thenReturn(Optional.empty());
            when(componentFactory.create(any(), any())).thenReturn(instance);

            assertThat((Object) applicationContext.getInstance(FirstTestClass.class))
                .isEqualTo(instance);
        }
    }

    @Component
    private static class FirstTestClass {}
}