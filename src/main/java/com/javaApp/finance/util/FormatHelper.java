package com.javaApp.finance.util;

import com.javaApp.finance.model.Currency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("format")
public class FormatHelper {

    public String money(BigDecimal amount, Currency currency) {
        return FormatUtil.formatMoney(amount, currency);
    }

    public String symbol(Currency currency) {
        return FormatUtil.getCurrencySymbol(currency);
    }
}
