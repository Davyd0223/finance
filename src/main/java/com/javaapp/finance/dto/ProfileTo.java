package com.javaapp.finance.dto;

import com.javaapp.finance.model.Currency;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileTo {

    private Integer id;

    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    private String email;

    private Currency defaultCurrency;

    @DecimalMin("0.0")
    private BigDecimal defaultMonthlyBudget;
}
