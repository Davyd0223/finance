package com.javaApp.finance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private String email;
    private String password;
    private Currency defaultCurrency;
    private BigDecimal defaultMonthlyBudget;
}
