package com.javaapp.finance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @NotNull(message = "Категория не может быть пустой")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @NotNull(message = "Дата не может быть пустой")
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @NotNull(message = "Сумма не может быть пустой")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше нуля")
    @Digits(integer = 12, fraction = 2, message = "Некорректный формат суммы")
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Тип операции не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(name = "operation_kind", nullable = false)
    private OperationKind kind;
}
