package com.fabbroniko.sdi.context;

import com.fabbroniko.sdi.annotation.Prototype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AnnotationBasedStrategySelectorTest {

    @Mock
    private Strategy singletonStrategy;
    @Mock
    private Strategy prototypeStrategy;

    private StrategySelector strategySelector;

    @BeforeEach
    void setUp() {
        strategySelector = new AnnotationBasedStrategySelector(singletonStrategy, prototypeStrategy);
    }

    @Test
    void shouldReturnPrototypeStrategy() {
        assertThat(strategySelector.select(PrototypeClass.class))
            .isEqualTo(prototypeStrategy);
    }

    @Test
    void shouldReturnSingletonStrategy() {
        assertThat(strategySelector.select(SingletonClass.class))
            .isEqualTo(singletonStrategy);
    }

    private static class SingletonClass {}

    @Prototype
    private static class PrototypeClass {}
}