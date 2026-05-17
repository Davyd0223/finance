package com.javaApp.finance.util;

import com.javaApp.finance.model.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatUtil {
    public static String formatMoney(BigDecimal amount, Currency currency) {
        if (amount == null) {
            return "0 " + getCurrencySymbol(currency);
        }
        return amount.setScale(2, RoundingMode.HALF_UP) + " " + getCurrencySymbol(currency);
    }

    public static String getCurrencySymbol(Currency currency) {
        if (currency == null) {
            return "";
        }
        return switch (currency) {
            case USD -> "$";
            case EUR -> "€";
            case PLN -> "zł";
        };
    }
}
