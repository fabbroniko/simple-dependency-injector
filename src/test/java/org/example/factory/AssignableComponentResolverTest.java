package org.example.factory;

import org.example.exception.InvalidDependencyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AssignableComponentResolverTest {

    @InjectMocks
    private AssignableComponentResolver componentResolver;

    @Test
    void shouldReturnTargetWhenNotAnInterface() {
        assertThat(componentResolver.resolve(Set.of(), Object.class))
            .isEqualTo(Object.class);
    }

    @Test
    void shouldReturnComponentClassOfInterface() {
        assertThat(componentResolver.resolve(Set.of(HashMap.class), Map.class))
            .isEqualTo(HashMap.class);
    }

    @Test
    void shouldThrowExceptionWhenAssignableIsNotFound() {
        final Set<Class<?>> scannedComponents = Set.of();

        assertThatThrownBy(() -> componentResolver.resolve(scannedComponents, Map.class))
            .isInstanceOf(InvalidDependencyException.class);
    }
}