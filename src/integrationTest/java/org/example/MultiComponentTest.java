package org.example;

import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.target.multi.Cat;
import org.example.target.multi.DependsOnMulti;
import org.example.target.multi.Dog;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Configuration(componentScan = "org.example.target.multi")
public class MultiComponentTest {

    @Test
    void shouldLoadBothClassesWithSameInterface() {
        final ApplicationContext context = DependencyInjector.run(MultiComponentTest.class);

        assertThat((DependsOnMulti) context.getInstance(DependsOnMulti.class))
            .satisfies(multi -> assertThat(multi.cat()).isInstanceOf(Cat.class))
            .satisfies(multi -> assertThat(multi.dog()).isInstanceOf(Dog.class));
    }
}
