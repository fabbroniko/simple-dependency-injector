package com.fabbroniko.sdi;

import com.fabbroniko.sdi.annotation.Configuration;
import com.fabbroniko.sdi.context.ApplicationContext;
import com.fabbroniko.sdi.exception.CircularDependencyException;
import com.fabbroniko.sdi.exception.DependencyResolutionException;
import com.fabbroniko.sdi.target.circular.FirstCircularDependency;
import com.fabbroniko.sdi.target.context.DependsOnContext;
import com.fabbroniko.sdi.target.interfaced.DependsOnInterface;
import com.fabbroniko.sdi.target.interfacedcircular.A;
import com.fabbroniko.sdi.target.qualifier.Cat;
import com.fabbroniko.sdi.target.qualifier.DependsOnMulti;
import com.fabbroniko.sdi.target.qualifier.Dog;
import com.fabbroniko.sdi.target.util.NoDependenciesTestClass;
import com.fabbroniko.sdi.target.util.WithDependencyTestClass;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Configuration(componentScan = "com.fabbroniko.sdi.target")
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

        assertThatThrownBy(() -> context.getInstance(com.fabbroniko.sdi.target.multi.DependsOnMulti.class))
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
