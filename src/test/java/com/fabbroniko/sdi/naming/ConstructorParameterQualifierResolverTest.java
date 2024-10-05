package com.fabbroniko.sdi.naming;

import com.fabbroniko.sdi.exception.DependencyResolutionException;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConstructorParameterQualifierResolverTest {

    @Mock
    private QualifierResolver<Class<?>> classQualifierResolver;
    @Mock
    private Parameter constructorParam;

    private ConstructorParameterQualifierResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new ConstructorParameterQualifierResolver(classQualifierResolver, Set.of(Integer.class, Double.class));
    }

    @Test
    void shouldGetParameterType() {
        doReturn(Integer.class).when(constructorParam).getType();

        resolver.resolve(constructorParam);

        verify(constructorParam).getType();
    }

    @Test
    void shouldThrowExceptionWhenMultipleMatch() {
        doReturn(Object.class).when(constructorParam).getType();

        assertThatThrownBy(() -> resolver.resolve(constructorParam))
            .isInstanceOf(DependencyResolutionException.class)
            .hasMessage("More than one class of type java.lang.Object found. Use @Qualifier to specify the name of the component to use for null");
    }

    @Test
    void shouldDelegateToClassNameResolver() {
        doReturn(Integer.class).when(constructorParam).getType();

        resolver.resolve(constructorParam);

        verify(classQualifierResolver).resolve(Integer.class);
    }

    @Test
    void shouldReturnValueFromDelegate() {
        doReturn(Integer.class).when(constructorParam).getType();
        when(classQualifierResolver.resolve(any())).thenReturn("delegated");

        assertThat(resolver.resolve(constructorParam))
            .isEqualTo("delegated");
    }

    @Test
    void shouldThrowExceptionWhenNoneMatch() {
        doReturn(Map.class).when(constructorParam).getType();

        assertThatThrownBy(() -> resolver.resolve(constructorParam))
            .isInstanceOf(DependencyResolutionException.class)
            .hasMessage("Could not resolve dependency java.util.Map of null");
    }
}