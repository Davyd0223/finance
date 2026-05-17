package com.javaApp.finance.service;

import com.javaApp.finance.model.OperationKind;
import com.javaApp.finance.model.Transaction;
import com.javaApp.finance.model.User;
import com.javaApp.finance.model.Wallet;
import com.javaApp.finance.repository.TransactionRepository;
import com.javaApp.finance.repository.WalletRepository;
import com.javaApp.finance.util.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.javaApp.finance.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private Messages messages;
    @InjectMocks
    private TransactionService transactionService;

    private User user;
    private Wallet wallet;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        user = createUser();
        wallet = createWallet(user);
        transaction = createTransaction(user, wallet);
    }

    @Test
    void create_whenValid_shouldSave() {
        transaction.setId(null);
        when(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        assertThat(transactionService.create(transaction, USER_ID)).isEqualTo(transaction);
    }

    @Test
    void create_whenIdNotNull_shouldThrow() {
        when(messages.get("error.id.mustBeNull")).thenReturn("ID должен быть null");

        assertThatThrownBy(() -> transactionService.create(transaction, USER_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void calculateBalance_shouldReturnIncomeMinusExpense() {
        Transaction expense = createTransaction(user, wallet, AMOUNT_200, OperationKind.EXPENSE);
        when(transactionRepository.findAllByUserIdOrderByDateTimeDesc(USER_ID))
                .thenReturn(List.of(transaction, expense));

        assertThat(transactionService.calculateBalance(USER_ID))
                .isEqualByComparingTo(new BigDecimal("300.00"));
    }
}