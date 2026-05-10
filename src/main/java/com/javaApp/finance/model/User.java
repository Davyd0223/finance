package com.javaApp.finance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends AbstractBaseEntity {
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_currency", nullable = false)
    private Currency defaultCurrency;

    @Column(name = "default_monthly_budget", nullable = false)
    private BigDecimal defaultMonthlyBudget;
}
