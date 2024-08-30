package org.example;

import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.exception.CircularDependencyException;
import org.example.target.circular.FirstCircularDependency;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Configuration(componentScan = "org.example.target.circular")
public class CircularDependencyTest {

    @Test
    void shouldThrowCircularDependencyException() {
        final ApplicationContext context = DependencyInjector.run(CircularDependencyTest.class);

        assertThatThrownBy(() -> context.getInstance(FirstCircularDependency.class))
            .isInstanceOf(CircularDependencyException.class);
    }
}
