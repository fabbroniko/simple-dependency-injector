package org.example;

import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.target.qualifier.Cat;
import org.example.target.qualifier.DependsOnMulti;
import org.example.target.qualifier.Dog;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Configuration(componentScan = "org.example.target.qualifier")
public class QualifierComponentTest {

    @Test
    void shouldLoadBothClassesWithSameInterface() {
        final ApplicationContext context = DependencyInjector.run(QualifierComponentTest.class);

        assertThat((DependsOnMulti) context.getInstance(DependsOnMulti.class))
            .satisfies(multi -> assertThat(multi.animal()).isInstanceOf(Cat.class))
            .satisfies(multi -> assertThat(multi.secondAnimal()).isInstanceOf(Dog.class));
    }
}
