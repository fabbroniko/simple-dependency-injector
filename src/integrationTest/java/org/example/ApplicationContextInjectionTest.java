package org.example;

import org.example.annotation.Configuration;
import org.example.target.context.DependsOnContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Configuration(componentScan = "org.example.target.context")
public class ApplicationContextInjectionTest {

    @Test
    void shouldInjectApplicationContext() {
        DependsOnContext target = DependencyInjector.run(ApplicationContextInjectionTest.class)
            .getInstance(DependsOnContext.class);

        assertThat(target.applicationContext()).isNotNull();
    }
}
