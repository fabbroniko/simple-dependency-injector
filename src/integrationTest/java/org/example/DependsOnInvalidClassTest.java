package org.example;

import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.exception.InvalidDependencyException;
import org.example.target.invalid.DependsOnObject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Configuration(componentScan = "org.example.target.invalid")
public class DependsOnInvalidClassTest {

    @Test
    void shouldThrowException() {
        final ApplicationContext context = DependencyInjector.run(DependsOnInvalidClassTest.class);

        assertThatThrownBy(() -> context.getInstance(DependsOnObject.class))
            .isInstanceOf(InvalidDependencyException.class);
    }
}
