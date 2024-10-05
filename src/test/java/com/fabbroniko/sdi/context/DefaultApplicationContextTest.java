package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.sdi.exception.ComponentDefinitionException;
import com.fabbroniko.sdi.factory.ComponentResolver;
import com.fabbroniko.sdi.naming.QualifierResolver;
import com.fabbroniko.ul.Logger;
import com.fabbroniko.ul.manager.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultApplicationContextTest {

    @Mock
    private Set<Class<?>> scannedComponents;
    @Mock
    private StrategySelector strategySelector;
    @Mock
    private ComponentResolver componentResolver;
    @Mock
    private QualifierResolver<Class<?>> nameResolver;
    @Mock
    private LogManager logManager;
    @Mock
    private Logger logger;
    @Mock
    private FirstTestClass instance;
    @Mock
    private Strategy strategy;

    private DefaultApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        when(logManager.getLogger(any())).thenReturn(logger);
        when(strategySelector.select(any())).thenReturn(strategy);

        applicationContext = new DefaultApplicationContext(logManager,
            scannedComponents,
            strategySelector,
            componentResolver,
            nameResolver);
    }

    @Test
    void shouldResolveName() {
        doReturn(FirstTestClass.class).when(componentResolver).resolve(any(), any(), any());

        applicationContext.getInstance(FirstTestClass.class);

        verify(nameResolver).resolve(FirstTestClass.class);
    }

    @Test
    void shouldLogGetInstance() {
        doReturn(FirstTestClass.class).when(componentResolver).resolve(any(), any(), any());

        applicationContext.getInstance(FirstTestClass.class);

        verify(logger).trace("get_instance", "com.fabbroniko.sdi.context.DefaultApplicationContextTest$FirstTestClass", null);
    }

    @Test
    void shouldReturnSelfWhenTargetIsApplicationContext() {
        reset(strategySelector);

        assertThat((Object) applicationContext.getInstance(ApplicationContext.class))
            .isEqualTo(applicationContext);
    }

    @Test
    void shouldResolveClassToInitialize() {
        doReturn(FirstTestClass.class).when(componentResolver).resolve(any(), any(), any());
        when(nameResolver.resolve(any())).thenReturn("integer");

        applicationContext.getInstance(FirstTestClass.class);

        verify(componentResolver).resolve(scannedComponents, FirstTestClass.class, "integer");
    }

    @Test
    void shouldLogResolveTargetClass() {
        doReturn(FirstTestClass.class).when(componentResolver).resolve(any(), any(), any());

        applicationContext.getInstance(FirstTestClass.class);

        verify(logger).trace(
            "resolve_target_class",
            "com.fabbroniko.sdi.context.DefaultApplicationContextTest$FirstTestClass",
            null,
            "com.fabbroniko.sdi.context.DefaultApplicationContextTest$FirstTestClass");
    }

    @Test
    void shouldThrowExceptionWhenNonComponent() {
        reset(strategySelector);
        when(componentResolver.resolve(any(), any(), any())).thenReturn(Object.class);

        assertThatThrownBy(() -> applicationContext.getInstance(Object.class))
            .isInstanceOf(ComponentDefinitionException.class)
            .hasMessage("Resolved target class java.lang.Object is not a Component");
    }

    @Test
    void shouldSelectStrategy() {
        doReturn(FirstTestClass.class).when(componentResolver).resolve(any(), any(), any());

        applicationContext.getInstance(FirstTestClass.class);

        verify(strategySelector).select(FirstTestClass.class);
    }

    @Test
    void shouldGetInstance() {
        doReturn(FirstTestClass.class).when(componentResolver).resolve(any(), any(), any());

        applicationContext.getInstance(FirstTestClass.class);

        verify(strategy).getInstance(FirstTestClass.class, applicationContext);
    }

    @Test
    void shouldReturnValueFromStrategy() {
        doReturn(FirstTestClass.class).when(componentResolver).resolve(any(), any(), any());
        when(strategy.getInstance(any(), any())).thenReturn(instance);

        assertThat(applicationContext.getInstance(FirstTestClass.class))
            .isEqualTo(instance);
    }

    @Component
    private static class FirstTestClass {}
}