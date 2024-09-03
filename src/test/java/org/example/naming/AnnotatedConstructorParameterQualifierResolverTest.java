package org.example.naming;

import org.example.annotation.Qualifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnnotatedConstructorParameterQualifierResolverTest {

    @Mock
    private QualifierResolver<Parameter> fallbackResolver;
    @Mock
    private Parameter parameter;
    @Mock
    private Qualifier qualifier;
    @InjectMocks
    private AnnotatedConstructorParameterQualifierResolver resolver;

    @Test
    void shouldReturnValueFromAnnotation() {
        when(parameter.isAnnotationPresent(any())).thenReturn(true);
        when(parameter.getAnnotation(any())).thenReturn(qualifier);
        when(qualifier.value()).thenReturn("annotated");

        assertThat(resolver.resolve(parameter)).isEqualTo("annotated");
    }

    @Test
    void shouldNotDelegateResolution() {
        when(parameter.isAnnotationPresent(any())).thenReturn(true);
        when(parameter.getAnnotation(any())).thenReturn(qualifier);
        when(qualifier.value()).thenReturn("annotated");

        resolver.resolve(parameter);

        verify(fallbackResolver, never()).resolve(any());
    }

    @Test
    void shouldDelegateResolutionToFallbackResolver() {
        when(parameter.isAnnotationPresent(any())).thenReturn(false);

        resolver.resolve(parameter);

        verify(fallbackResolver).resolve(parameter);
    }

    @Test
    void shouldReturnValueFromDelegate() {
        when(parameter.isAnnotationPresent(any())).thenReturn(false);
        when(fallbackResolver.resolve(any())).thenReturn("delegated");

        assertThat(resolver.resolve(parameter)).isEqualTo("delegated");
    }
}