package com.fabbroniko.sdi.factory;

import com.fabbroniko.sdi.context.ApplicationContext;
import com.fabbroniko.sdi.exception.InvalidComponentConstructorException;
import com.fabbroniko.sdi.naming.QualifierResolver;
import com.fabbroniko.ul.Logger;
import com.fabbroniko.ul.manager.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultComponentFactoryTest {

    @Mock
    private QualifierResolver<Parameter> nameResolver;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private Set<Object> firstArgument;
    @Mock
    private Map<String, String> secondArgument;
    @Mock
    private LogManager logManager;
    @Mock
    private Logger logger;

    private DefaultComponentFactory componentFactory;

    @BeforeEach
    void setUp() {
        when(logManager.getLogger(any())).thenReturn(logger);
        when(applicationContext.getInstance(eq(Set.class), anyString())).thenReturn(firstArgument);
        when(applicationContext.getInstance(eq(Map.class), anyString())).thenReturn(secondArgument);

        componentFactory = new DefaultComponentFactory(nameResolver, logManager);
    }

    @Test
    void shouldThrowExceptionIfSuitableConstructorNotFound() {
        reset(applicationContext);

        assertThatThrownBy(() -> componentFactory.create(ConstructorLess.class, applicationContext))
            .isInstanceOf(InvalidComponentConstructorException.class);
    }

    @Test
    void shouldResolveNameFirstParameter() {
        when(nameResolver.resolve(any())).thenReturn("setType").thenReturn("mapType");

        componentFactory.create(WithConstructor.class, applicationContext);

        verify(nameResolver, times(2)).resolve(any());
    }

    @Test
    void shouldGetFirstConstructorArgumentFromContext() {
        when(nameResolver.resolve(any())).thenReturn("setType").thenReturn("mapType");

        componentFactory.create(WithConstructor.class, applicationContext);

        verify(applicationContext).getInstance(Set.class, "setType");
    }

    @Test
    void shouldGetSecondConstructorArgumentFromContext() {
        when(nameResolver.resolve(any())).thenReturn("setType").thenReturn("mapType");

        componentFactory.create(WithConstructor.class, applicationContext);

        verify(applicationContext).getInstance(Map.class, "mapType");
    }

    @Test
    void shouldReturnConstructedObject() {
        when(nameResolver.resolve(any())).thenReturn("setType").thenReturn("mapType");

        assertThat(componentFactory.create(WithConstructor.class, applicationContext))
            .isInstanceOf(WithConstructor.class);
    }

    @Test
    void shouldCreateLogger() {
        reset(applicationContext);

        componentFactory.create(WithLogger.class, applicationContext);

        verify(logManager).getLogger(WithLogger.class);
    }

    @Test
    void shouldLogCreateLogger() {
        reset(applicationContext);

        componentFactory.create(WithLogger.class, applicationContext);

        verify(logger).trace("returning_logger",
            "com.fabbroniko.sdi.factory.DefaultComponentFactoryTest$WithLogger",
            "arg0",
            "com.fabbroniko.ul.Logger");
    }

    @Test
    void shouldGetInstanceOfApplicationContext() {
        reset(applicationContext);

        componentFactory.create(WithApplicationContext.class, applicationContext);

        verify(applicationContext).getInstance(ApplicationContext.class);
    }

    @Test
    void shouldLogGetInstanceOfApplicationContext() {
        reset(applicationContext);

        componentFactory.create(WithApplicationContext.class, applicationContext);

        verify(logger).trace("returning_context",
            "com.fabbroniko.sdi.factory.DefaultComponentFactoryTest$WithApplicationContext",
            "arg0",
            "com.fabbroniko.sdi.context.ApplicationContext");
    }

    @Test
    void shouldLogFirstParameterToInstance() {
        when(nameResolver.resolve(any())).thenReturn("setType").thenReturn("mapType");

        componentFactory.create(WithConstructor.class, applicationContext);

        verify(logger).trace("parameter_to_instance",
            "com.fabbroniko.sdi.factory.DefaultComponentFactoryTest$WithConstructor",
            "arg0",
            "java.util.Set");
    }

    @Test
    void shouldLogSecondParameterToInstance() {
        when(nameResolver.resolve(any())).thenReturn("setType").thenReturn("mapType");

        componentFactory.create(WithConstructor.class, applicationContext);

        verify(logger).trace("parameter_to_instance",
            "com.fabbroniko.sdi.factory.DefaultComponentFactoryTest$WithConstructor",
            "arg1",
            "java.util.Map");
    }

    private static class ConstructorLess {

        private ConstructorLess() {}
    }

    private static class WithConstructor {

        public WithConstructor(final Set<Object> firstArgument, final Map<String, String> secondArgument) {}
    }

    private static class WithLogger {

        public WithLogger(final Logger logger) {}
    }

    private static class WithApplicationContext {

        public WithApplicationContext(final ApplicationContext applicationContext) {}
    }
}