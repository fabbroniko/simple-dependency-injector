package org.example;

import org.example.annotation.Configuration;
import org.example.context.ApplicationContext;
import org.example.exception.CircularDependencyException;
import org.example.exception.DependencyResolutionException;
import org.example.target.circular.FirstCircularDependency;
import org.example.target.context.DependsOnContext;
import org.example.target.interfaced.DependsOnInterface;
import org.example.target.interfacedcircular.A;
import org.example.target.qualifier.Cat;
import org.example.target.qualifier.DependsOnMulti;
import org.example.target.qualifier.Dog;
import org.example.target.util.NoDependenciesTestClass;
import org.example.target.util.WithDependencyTestClass;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Configuration(componentScan = "org.example.target")
public class DependencyInjectorTest {

    @Test
    void shouldCreateInstanceOfWithDependencyTestClass() {
        WithDependencyTestClass target = DependencyInjector.run(DependencyInjectorTest.class)
            .getInstance(WithDependencyTestClass.class);

        assertThat(target.dependency())
            .isNotNull();
    }

    @Test
    void shouldLoadBothClassesWithSameInterface() {
        final ApplicationContext context = DependencyInjector.run(DependencyInjectorTest.class);

        assertThat((DependsOnMulti) context.getInstance(DependsOnMulti.class))
            .satisfies(multi -> assertThat(multi.animal()).isInstanceOf(Cat.class))
            .satisfies(multi -> assertThat(multi.secondAnimal()).isInstanceOf(Dog.class));
    }

    @Test
    void shouldCreateInstanceOfNoDependenciesTestClass() {
        NoDependenciesTestClass target = DependencyInjector.run(DependencyInjectorTest.class)
            .getInstance(NoDependenciesTestClass.class);

        assertThat(target).isNotNull();
    }

    @Test
    void shouldLoadBothClassesWithSameInterface2() {
        final ApplicationContext context = DependencyInjector.run(DependencyInjectorTest.class);

        assertThatThrownBy(() -> context.getInstance(org.example.target.multi.DependsOnMulti.class))
            .isInstanceOf(DependencyResolutionException.class);
    }

    @Test
    void shouldThrowException() {
        final ApplicationContext context = DependencyInjector.run(DependencyInjectorTest.class);

        assertThatThrownBy(() -> context.getInstance(A.class))
            .isInstanceOf(CircularDependencyException.class);
    }

    @Test
    void shouldInjectInterface() {
        DependsOnInterface target = DependencyInjector.run(DependencyInjectorTest.class)
            .getInstance(DependsOnInterface.class);

        assertThat(target.sampleInterface()).isNotNull();
    }

    @Test
    void shouldThrowCircularDependencyException() {
        final ApplicationContext context = DependencyInjector.run(DependencyInjectorTest.class);

        assertThatThrownBy(() -> context.getInstance(FirstCircularDependency.class))
            .isInstanceOf(CircularDependencyException.class);
    }

    @Test
    void shouldInjectApplicationContext() {
        DependsOnContext target = DependencyInjector.run(DependencyInjectorTest.class)
            .getInstance(DependsOnContext.class);

        assertThat(target.applicationContext()).isNotNull();
    }
}
