package com.fabbroniko.sdi;

import com.fabbroniko.sdi.context.ApplicationContext;
import com.fabbroniko.sdi.exception.CircularDependencyException;
import com.fabbroniko.sdi.exception.DependencyResolutionException;
import com.fabbroniko.sdi.target.circular.FirstCircularDependency;
import com.fabbroniko.sdi.target.context.DependsOnContext;
import com.fabbroniko.sdi.target.interfaced.DependsOnInterface;
import com.fabbroniko.sdi.target.interfacedcircular.A;
import com.fabbroniko.sdi.target.log.WithLoggerDependency;
import com.fabbroniko.sdi.target.qualifier.Cat;
import com.fabbroniko.sdi.target.qualifier.DependsOnMulti;
import com.fabbroniko.sdi.target.qualifier.Dog;
import com.fabbroniko.sdi.target.util.NoDependenciesTestClass;
import com.fabbroniko.sdi.target.util.WithDependencyTestClass;
import com.fabbroniko.ul.FormattedLogger;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DependencyInjectorTest {

    @Test
    void shouldCreateInstanceOfWithDependencyTestClass() {
        WithDependencyTestClass target = DependencyInjector.run(WithDependencyTestClass.class)
            .getInstance(WithDependencyTestClass.class);

        assertThat(target.dependency())
            .isNotNull();
    }

    @Test
    void shouldLoadBothClassesWithSameInterface() {
        final ApplicationContext context = DependencyInjector.run(DependsOnMulti.class);

        assertThat((DependsOnMulti) context.getInstance(DependsOnMulti.class))
            .satisfies(multi -> assertThat(multi.animal()).isInstanceOf(Cat.class))
            .satisfies(multi -> assertThat(multi.secondAnimal()).isInstanceOf(Dog.class));
    }

    @Test
    void shouldCreateInstanceOfNoDependenciesTestClass() {
        NoDependenciesTestClass target = DependencyInjector.run(NoDependenciesTestClass.class)
            .getInstance(NoDependenciesTestClass.class);

        assertThat(target).isNotNull();
    }

    @Test
    void shouldLoadBothClassesWithSameInterface2() {
        final ApplicationContext context = DependencyInjector.run(com.fabbroniko.sdi.target.multi.DependsOnMulti.class);

        assertThatThrownBy(() -> context.getInstance(com.fabbroniko.sdi.target.multi.DependsOnMulti.class))
            .isInstanceOf(DependencyResolutionException.class);
    }

    @Test
    void shouldThrowException() {
        final ApplicationContext context = DependencyInjector.run(A.class);

        assertThatThrownBy(() -> context.getInstance(A.class))
            .isInstanceOf(CircularDependencyException.class);
    }

    @Test
    void shouldInjectInterface() {
        DependsOnInterface target = DependencyInjector.run(DependsOnInterface.class)
            .getInstance(DependsOnInterface.class);

        assertThat(target.sampleInterface()).isNotNull();
    }

    @Test
    void shouldThrowCircularDependencyException() {
        final ApplicationContext context = DependencyInjector.run(FirstCircularDependency.class);

        assertThatThrownBy(() -> context.getInstance(FirstCircularDependency.class))
            .isInstanceOf(CircularDependencyException.class);
    }

    @Test
    void shouldInjectApplicationContext() {
        DependsOnContext target = DependencyInjector.run(DependsOnContext.class)
            .getInstance(DependsOnContext.class);

        assertThat(target.applicationContext()).isNotNull();
    }

    @Test
    void shouldInjectLogger() {
        WithLoggerDependency target = DependencyInjector.run(WithLoggerDependency.class)
            .getInstance(WithLoggerDependency.class);

        assertThat(target.logger()).isInstanceOf(FormattedLogger.class);
    }
}
