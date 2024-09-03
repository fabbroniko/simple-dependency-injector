package org.example.naming;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class QualifierValidatorTest {

    @InjectMocks
    private QualifierValidator validator;

    @Test
    void shouldReturnFalseWhenNull() {
        assertThat(validator.isValid(null)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenEmpty() {
        assertThat(validator.isValid("")).isFalse();
    }

    @Test
    void shouldReturnFalseWhenWhitespacesOnly() {
        assertThat(validator.isValid("       ")).isFalse();
    }

    @Test
    void shouldReturnTrueWithValidString() {
        assertThat(validator.isValid("valid")).isTrue();
    }
}