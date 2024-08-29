package org.example;

import org.example.annotation.Configuration;
import org.example.exception.InvalidDependencyException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Configuration(componentScan = "org.example.target.invalid")
public class DependsOnInvalidClassTest {

    @Test
    void shouldThrowException() {
        assertThatThrownBy(() -> DependencyInjector.run(DependsOnInvalidClassTest.class))
            .isInstanceOf(InvalidDependencyException.class);
    }
}
