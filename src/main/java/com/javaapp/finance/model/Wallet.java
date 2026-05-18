package com.javaapp.finance.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallets")
public class Wallet extends AbstractBaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull()
    @Size(min = 2, max = 20)
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Валюта не может быть пустой")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @NotNull(message = "Тип кошелька не может быть пустым")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType type;
}
