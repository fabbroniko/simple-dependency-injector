package org.example;

import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.exception.DependencyResolutionException;
import org.example.target.multi.DependsOnMulti;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Configuration(componentScan = "org.example.target.multi")
public class MultiComponentTest {

    @Test
    void shouldLoadBothClassesWithSameInterface() {
        final ApplicationContext context = DependencyInjector.run(MultiComponentTest.class);

        assertThatThrownBy(() -> context.getInstance(DependsOnMulti.class))
            .isInstanceOf(DependencyResolutionException.class);
    }
}
