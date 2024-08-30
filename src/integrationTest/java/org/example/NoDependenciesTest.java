package org.example;

import org.example.annotation.Configuration;
import org.example.target.util.NoDependenciesTestClass;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Configuration(componentScan = "org.example.target.util")
public class NoDependenciesTest {

    @Test
    void shouldCreateInstanceOfNoDependenciesTestClass() {
        NoDependenciesTestClass target = DependencyInjector.run(NoDependenciesTest.class)
            .getInstance(NoDependenciesTestClass.class);

        assertThat(target).isNotNull();
    }
}
