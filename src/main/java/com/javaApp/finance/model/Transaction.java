package com.javaApp.finance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction extends AbstractBaseEntity {
    @ManyToOne
    private User user;
    @OneToOne
    private Wallet wallet;
    @OneToOne
    private OperationCategory category;
    private LocalDateTime date_time;
    private BigDecimal amount;
    private OperationKind kind;
    private String description;
}
