package org.example;

import org.example.annotation.Configuration;
import org.example.exception.CircularDependencyException;
import org.example.target.interfaced.DependsOnInterface;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Configuration(componentScan = "org.example.target.interfacedcircular")
public class InterfacedCircularDependencyTest {

    @Test
    void shouldInjectInterface() {
        assertThatThrownBy(() -> DependencyInjector.run(InterfacedCircularDependencyTest.class))
            .isInstanceOf(CircularDependencyException.class);
    }
}
