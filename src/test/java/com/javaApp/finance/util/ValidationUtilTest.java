package com.javaApp.finance.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationUtilTest {

    @Test
    void requireNotBlank_whenBlank_shouldAddError() {
        var v = new ValidationUtil().requireNotBlank("", "ошибка");
        assertThat(v.hasErrors()).isTrue();
    }

    @Test
    void requirePositive_whenZero_shouldAddError() {
        var v = new ValidationUtil().requirePositive(BigDecimal.ZERO, "ошибка");
        assertThat(v.hasErrors()).isTrue();
    }

    @Test
    void chainValidation_noErrors_shouldBeClean() {
        var v = new ValidationUtil()
                .requireNotBlank("Иван", "ошибка")
                .requirePositive(new BigDecimal("100"), "ошибка");
        assertThat(v.hasErrors()).isFalse();
    }
}