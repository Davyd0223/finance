package com.javaapp.finance.util;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationUtil {
    private final List<String> errors = new ArrayList<>();

    public ValidationUtil requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            errors.add(message);
        }
        return this;
    }

    public ValidationUtil requireMinLength(String value, int min, String message) {
        if (value != null && !value.isBlank() && value.length() < min) {
            errors.add(message);
        }
        return this;
    }

    public ValidationUtil requirePositive(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(message);
        }
        return this;
    }

    public ValidationUtil requireNotNegative(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            errors.add(message);
        }
        return this;
    }

    public ValidationUtil requireNotNull(Object value, String message) {
        if (value == null) {
            errors.add(message);
        }
        return this;
    }

    public ValidationUtil requireDigits(BigDecimal value, int maxIntegerDigits, int maxFractionDigits, String message) {
        if (value != null) {
            int integerDigits = value.precision() - value.scale();
            int fractionDigits = Math.max(value.scale(), 0);

            if (integerDigits > maxIntegerDigits || fractionDigits > maxFractionDigits) {
                errors.add(message);
            }
        }
        return this;
    }

    public ValidationUtil requireMaxLength(String value, int max, String message) {
        if (value != null && value.length() > max) {
            errors.add(message);
        }
        return this;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

}
