package org.example;

import org.example.annotation.Configuration;
import org.example.exception.CircularDependencyException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Configuration(componentScan = "org.example.target.circular")
public class CircularDependencyTest {

    @Test
    void shouldThrowCircularDependencyException() {
        assertThatThrownBy(() -> DependencyInjector.run(CircularDependencyTest.class))
            .isInstanceOf(CircularDependencyException.class);
    }
}
