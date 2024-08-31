package org.example;

import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.exception.CircularDependencyException;
import org.example.target.interfacedcircular.A;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Configuration(componentScan = "org.example.target.interfacedcircular")
public class InterfacedCircularDependencyTest {

    @Test
    void shouldThrowException() {
        final ApplicationContext context = DependencyInjector.run(InterfacedCircularDependencyTest.class);

        assertThatThrownBy(() -> context.getInstance(A.class))
            .isInstanceOf(CircularDependencyException.class);
    }
}
