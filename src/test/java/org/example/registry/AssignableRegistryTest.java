package org.example.registry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignableRegistryTest {

    @Mock
    private Map<Class<?>, Instance> registry;
    @Mock
    private InstanceFactory instanceFactory;
    @Mock
    private Instance instance;
    @Mock
    private Object actualInstance;
    @InjectMocks
    private AssignableRegistry assignableRegistry;

    @Test
    void shouldPutProcessingInstanceInRegistry() {
        when(instanceFactory.createProcessing()).thenReturn(instance);

        assignableRegistry.process(Object.class);

        verify(registry).put(Object.class, instance);
    }

    @Test
    void shouldPutCompletedInstanceInRegistry() {
        when(instanceFactory.createCompleted(actualInstance)).thenReturn(instance);

        assignableRegistry.insert(Object.class, actualInstance);

        verify(registry).put(Object.class, instance);
    }

    @Nested
    class GetInstanceTest {

        @Test
        void shouldGetInstanceOfSameClass() {
            when(registry.keySet()).thenReturn(Set.of(Object.class));

            assignableRegistry.getInstance(Object.class);

            verify(registry).get(Object.class);
        }

        @Test
        void shouldGetInstanceOfSuperClass() {
            when(registry.keySet()).thenReturn(Set.of(Integer.class));

            assignableRegistry.getInstance(Object.class);

            verify(registry).get(Integer.class);
        }

        @Test
        void shouldGetInstanceOfInterface() {
            when(registry.keySet()).thenReturn(Set.of(HashMap.class));

            assignableRegistry.getInstance(Map.class);

            verify(registry).get(HashMap.class);
        }

        @Test
        void shouldReturnInstanceFromRegistry() {
            when(registry.keySet()).thenReturn(Set.of(Object.class));
            when(registry.get(Object.class)).thenReturn(instance);
            when(instance.instance()).thenReturn(actualInstance);

            assertThat(assignableRegistry.getInstance(Object.class))
                .contains(actualInstance);
        }
    }

    @Nested
    class IsProcessingTest {

        @Test
        void shouldGetInstanceOfSameClass() {
            when(registry.keySet()).thenReturn(Set.of(Object.class));

            assignableRegistry.isProcessing(Object.class);

            verify(registry).get(Object.class);
        }

        @Test
        void shouldGetInstanceOfSuperClass() {
            when(registry.keySet()).thenReturn(Set.of(Integer.class));

            assignableRegistry.isProcessing(Object.class);

            verify(registry).get(Integer.class);
        }

        @Test
        void shouldGetInstanceOfInterface() {
            when(registry.keySet()).thenReturn(Set.of(HashMap.class));

            assignableRegistry.isProcessing(Map.class);

            verify(registry).get(HashMap.class);
        }

        @Test
        void shouldGetInstanceState() {
            when(registry.keySet()).thenReturn(Set.of(Object.class));
            when(registry.get(Object.class)).thenReturn(instance);

            assignableRegistry.isProcessing(Object.class);

            verify(instance).state();
        }

        @Test
        void shouldReturnFalseWhenNotProcessing() {
            when(registry.keySet()).thenReturn(Set.of(Object.class));
            when(registry.get(Object.class)).thenReturn(instance);
            when(instance.state()).thenReturn(State.COMPLETE);

            assertThat(assignableRegistry.isProcessing(Object.class))
                .isFalse();
        }

        @Test
        void shouldReturnTrueWhenProcessing() {
            when(registry.keySet()).thenReturn(Set.of(Object.class));
            when(registry.get(Object.class)).thenReturn(instance);
            when(instance.state()).thenReturn(State.COMPLETE);

            assertThat(assignableRegistry.isProcessing(Object.class))
                .isFalse();
        }
    }
}