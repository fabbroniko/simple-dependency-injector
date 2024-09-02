package org.example.factory;

import org.example.context.ApplicationContext;
import org.example.exception.InvalidComponentConstructorException;
import org.example.naming.ConstructorParameterNameResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

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
class ComponentFactoryImplTest {

    @Mock
    private ConstructorParameterNameResolver nameResolver;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private Object firstArgument;
    @Mock
    private Map<String, String> secondArgument;
    @InjectMocks
    private ComponentFactoryImpl componentFactory;

    @BeforeEach
    void setUp() {
        when(applicationContext.getInstance(eq(Object.class), anyString())).thenReturn(firstArgument);
        when(applicationContext.getInstance(eq(Map.class), anyString())).thenReturn(secondArgument);
        when(nameResolver.resolve(any())).thenReturn("objectType").thenReturn("mapType");
    }

    @Test
    void shouldThrowExceptionIfSuitableConstructorNotFound() {
        reset(applicationContext);
        reset(nameResolver);

        assertThatThrownBy(() -> componentFactory.create(ConstructorLess.class, applicationContext))
            .isInstanceOf(InvalidComponentConstructorException.class);
    }

    @Test
    void shouldResolveNameFirstParameter() {
        componentFactory.create(WithConstructor.class, applicationContext);

        verify(nameResolver, times(2)).resolve(any());
    }

    @Test
    void shouldGetFirstConstructorArgumentFromContext() {
        componentFactory.create(WithConstructor.class, applicationContext);

        verify(applicationContext).getInstance(Object.class, "objectType");
    }

    @Test
    void shouldGetSecondConstructorArgumentFromContext() {
        componentFactory.create(WithConstructor.class, applicationContext);

        verify(applicationContext).getInstance(Map.class, "mapType");
    }

    @Test
    void shouldReturnConstructedObject() {
        assertThat(componentFactory.create(WithConstructor.class, applicationContext))
            .isInstanceOf(WithConstructor.class);
    }

    private static class ConstructorLess {

        private ConstructorLess() {}
    }

    private static class WithConstructor {

        public WithConstructor(final Object firstArgument, final Map<String, String> secondArgument) {}
    }
}