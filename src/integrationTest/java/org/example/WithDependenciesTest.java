package org.example;

import org.example.annotation.Configuration;
import org.example.target.util.WithDependencyTestClass;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Configuration(componentScan = "org.example.target.util")
public class WithDependenciesTest {

    @Test
    void shouldCreateInstanceOfWithDependencyTestClass() {
        WithDependencyTestClass target = DependencyInjector.run(WithDependenciesTest.class)
            .getInstance(WithDependencyTestClass.class);

        assertThat(target.dependency())
            .isNotNull();
    }
}
