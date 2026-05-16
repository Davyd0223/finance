package com.javaApp.finance;

import com.javaApp.finance.model.*;
import com.javaApp.finance.security.AuthUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestData {

    public static final Integer USER_ID = 1;
    public static final String USER_NAME = "Иван";
    public static final String USER_EMAIL = "ivan@test.com";
    public static final String USER_PASSWORD = "password123";
    public static final String USER_ENCODED_PASSWORD = "encoded_password";
    public static final Integer WALLET_ID = 10;
    public static final String WALLET_NAME = "Основной";
    public static final Integer TRANSACTION_ID = 100;
    public static final BigDecimal AMOUNT_500 = new BigDecimal("500.00");
    public static final BigDecimal AMOUNT_200 = new BigDecimal("200.00");

    public static User createUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_ENCODED_PASSWORD);
        user.setDefaultCurrency(Currency.USD);
        user.setDefaultMonthlyBudget(BigDecimal.ZERO);
        return user;
    }

    public static User createUser(Integer id, String name, String email) {
        User user = createUser();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    public static Wallet createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setId(WALLET_ID);
        wallet.setUser(user);
        wallet.setName(WALLET_NAME);
        wallet.setCurrency(Currency.USD);
        wallet.setType(WalletType.CASH);
        return wallet;
    }

    public static Transaction createTransaction(User user, Wallet wallet,
                                                BigDecimal amount, OperationKind kind) {
        Transaction t = new Transaction();
        t.setUser(user);
        t.setWallet(wallet);
        t.setAmount(amount);
        t.setKind(kind);
        t.setCategory(Category.SALARY);
        t.setDateTime(LocalDateTime.now());
        return t;
    }

    public static Transaction createTransaction(User user, Wallet wallet) {
        Transaction t = createTransaction(user, wallet, AMOUNT_500, OperationKind.INCOME);
        t.setId(TRANSACTION_ID);
        return t;
    }
}