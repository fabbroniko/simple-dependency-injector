package org.example.factory;

import org.example.context.ApplicationContext;
import org.example.exception.InvalidComponentConstructorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComponentFactoryImplTest {

    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private Object firstArgument;
    @Mock
    private Map<String, String> secondArgument;
    @InjectMocks
    private ComponentFactoryImpl componentFactory;

    @Test
    void shouldThrowExceptionIfSuitableConstructorNotFound() {
        assertThatThrownBy(() -> componentFactory.create(ConstructorLess.class, applicationContext))
            .isInstanceOf(InvalidComponentConstructorException.class);
    }

    @Test
    void shouldGetFirstConstructorArgumentFromContext() {
        when(applicationContext.getInstance(Object.class)).thenReturn(firstArgument);
        when(applicationContext.getInstance(Map.class)).thenReturn(secondArgument);

        componentFactory.create(WithConstructor.class, applicationContext);

        verify(applicationContext).getInstance(Object.class);
    }

    @Test
    void shouldGetSecondConstructorArgumentFromContext() {
        when(applicationContext.getInstance(Object.class)).thenReturn(firstArgument);
        when(applicationContext.getInstance(Map.class)).thenReturn(secondArgument);

        componentFactory.create(WithConstructor.class, applicationContext);

        verify(applicationContext).getInstance(Map.class);
    }

    @Test
    void shouldReturnConstructedObject() {
        when(applicationContext.getInstance(Object.class)).thenReturn(firstArgument);
        when(applicationContext.getInstance(Map.class)).thenReturn(secondArgument);

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