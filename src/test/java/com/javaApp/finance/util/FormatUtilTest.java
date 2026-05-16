package com.javaApp.finance.util;

import com.javaApp.finance.model.Currency;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class FormatUtilTest {

    @Test
    void formatMoney_shouldFormatCorrectly() {
        assertThat(FormatUtil.formatMoney(new BigDecimal("100"), Currency.USD))
                .isEqualTo("100.00 $");
    }

    @Test
    void formatMoney_whenNull_shouldReturnZero() {
        assertThat(FormatUtil.formatMoney(null, Currency.USD))
                .isEqualTo("0 $");
    }
}