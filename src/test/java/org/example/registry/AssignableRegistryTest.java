package org.example.registry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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
        void shouldGetValueFromRegistry() {
            doReturn(instance).when(registry).get(any(Class.class));

            assignableRegistry.getInstance(Object.class);

            verify(registry).get(Object.class);
        }

        @Test
        void shouldReturnInstanceFromRegistry() {
            doReturn(instance).when(registry).get(any(Class.class));
            when(instance.instance()).thenReturn(actualInstance);

            assertThat(assignableRegistry.getInstance(Object.class))
                .contains(actualInstance);
        }
    }

    @Nested
    class IsProcessingTest {

        @Test
        void shouldGetValueFromRegistry() {
            doReturn(instance).when(registry).get(any(Class.class));

            assignableRegistry.isProcessing(Object.class);

            verify(registry).get(Object.class);
        }

        @Test
        void shouldGetInstanceState() {
            doReturn(instance).when(registry).get(any(Class.class));

            assignableRegistry.isProcessing(Object.class);

            verify(instance).state();
        }

        @Test
        void shouldReturnFalseWhenNotProcessing() {
            doReturn(instance).when(registry).get(any(Class.class));
            when(instance.state()).thenReturn(State.COMPLETE);

            assertThat(assignableRegistry.isProcessing(Object.class))
                .isFalse();
        }

        @Test
        void shouldReturnTrueWhenProcessing() {
            doReturn(instance).when(registry).get(any(Class.class));
            when(instance.state()).thenReturn(State.PROCESSING);

            assertThat(assignableRegistry.isProcessing(Object.class))
                .isTrue();
        }
    }
}