package org.example;

import org.example.annotation.Configuration;
import org.example.target.interfaced.DependsOnInterface;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Configuration(componentScan = "org.example.target.interfaced")
public class DependsOnInterfaceTest {

    @Test
    void shouldInjectInterface() {
        DependsOnInterface target = DependencyInjector.run(DependsOnInterfaceTest.class)
            .getInstance(DependsOnInterface.class);

        assertThat(target.sampleInterface()).isNotNull();
    }
}
