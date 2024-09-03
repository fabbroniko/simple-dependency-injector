package org.example.naming;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ClassClassBasedQualifierResolverTest {

    @InjectMocks
    private ClassBasedQualifierResolver resolver;

    @ParameterizedTest
    @MethodSource("resolverTestData")
    void shouldReturnFormattedQualifier(final Class<?> input, final String expected) {
        assertThat(resolver.resolve(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> resolverTestData() {
        return Stream.of(
            Arguments.of(Object.class, "object"),
            Arguments.of(ClassBasedQualifierResolver.class, "classBasedQualifierResolver"),
            Arguments.of(Integer.class, "integer")
        );
    }
}