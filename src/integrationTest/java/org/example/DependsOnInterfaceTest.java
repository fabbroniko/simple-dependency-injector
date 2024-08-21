package org.example;

import org.example.annotation.Configuration;
import org.example.target.interfaced.DependsOnInterface;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Configuration(componentScan = "org.example.target.interfaced")
public class DependsOnInterfaceTest {

    @Test
    void shouldInjectInterface() {
        DependencyInjector.run(DependsOnInterfaceTest.class);

        assertThat(DependencyInjector.get(DependsOnInterface.class))
            .isNotNull()
            .extracting(DependsOnInterface::getSampleInterface)
            .isNotNull();
    }
}
