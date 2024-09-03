package org.example.naming;

import org.example.annotation.Component;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnnotationBasedQualifierResolverTest {

    @Mock
    private QualifierResolver<Class<?>> alternateResolver;
    @Mock
    private Validator validator;
    @InjectMocks
    private AnnotationBasedQualifierResolver qualifierResolver;

    @Test
    void shouldDelegateToAlternateWhenNotAnnotated() {
        qualifierResolver.resolve(NonAnnotated.class);

        verify(alternateResolver).resolve(NonAnnotated.class);
    }

    @Test
    void shouldDelegateToAlternateWhenInvalidName() {
        when(validator.isValid(anyString())).thenReturn(false);

        qualifierResolver.resolve(Annotated.class);

        verify(alternateResolver).resolve(Annotated.class);
    }

    @Test
    void shouldReturnValueFromDelegate() {
        when(alternateResolver.resolve(any())).thenReturn("delegated");

        assertThat(qualifierResolver.resolve(NonAnnotated.class))
            .isEqualTo("delegated");
    }

    @Test
    void shouldNotDelegateWithValidAnnotation() {
        when(validator.isValid(anyString())).thenReturn(true);

        qualifierResolver.resolve(Annotated.class);

        verify(alternateResolver, never()).resolve(any());
    }

    @Test
    void shouldReturnAnnotationValue() {
        when(validator.isValid(anyString())).thenReturn(true);

        assertThat(qualifierResolver.resolve(Annotated.class))
            .isEqualTo("valid");
    }

    private static class NonAnnotated {}

    @Component("valid")
    private static class Annotated {}
}