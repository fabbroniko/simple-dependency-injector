package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.factory.ComponentFactory;
import com.fabbroniko.ul.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrototypeStrategyTest {

    @Mock
    private Logger logger;
    @Mock
    private ComponentFactory componentFactory;
    @Mock
    private ApplicationContext applicationContext;

    private Strategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new PrototypeStrategy(logger, componentFactory);
    }

    @Test
    void shouldLog() {
        strategy.getInstance(Object.class, applicationContext);

        verify(logger).trace(
            "create_prototype_target",
            "java.lang.Object"
        );
    }

    @Test
    void shouldCreateInstance() {
        strategy.getInstance(Object.class, applicationContext);

        verify(componentFactory).create(Object.class, applicationContext);
    }

    @Test
    void shouldReturnValueFromFactory() {
        final Object expectedInstance = new Object();
        when(componentFactory.create(any(), any())).thenReturn(expectedInstance);

        assertThat(strategy.getInstance(Object.class, applicationContext))
            .isEqualTo(expectedInstance);
    }
}