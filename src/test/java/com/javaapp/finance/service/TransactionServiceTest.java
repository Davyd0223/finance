package com.javaapp.finance.service;

import com.javaapp.finance.model.OperationKind;
import com.javaapp.finance.model.Transaction;
import com.javaapp.finance.model.User;
import com.javaapp.finance.model.Wallet;
import com.javaapp.finance.repository.TransactionRepository;
import com.javaapp.finance.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.javaapp.finance.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

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

        when(walletRepository.findById(WALLET_ID))
                .thenReturn(Optional.of(wallet));
        when(transactionRepository.save(transaction))
                .thenReturn(transaction);

        assertThat(transactionService.create(transaction, USER_ID))
                .isEqualTo(transaction);

        verify(transactionRepository).save(transaction);
    }

    @Test
    void create_whenIdNotNull_shouldThrow() {
        assertThatThrownBy(() -> transactionService.create(transaction, USER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id must be null");
    }

    @Test
    void create_whenWrongOwner_shouldThrow() {
        transaction.setId(null);
        User otherUser = createUser(999, "Other", "other@test.com");
        transaction.setUser(otherUser);

        assertThatThrownBy(() -> transactionService.create(transaction, USER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Transaction does not belong to user");
    }

    @Test
    void getByIdAndUserId_whenNotFound_shouldThrow() {
        when(transactionRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getByIdAndUserId(999, USER_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Transaction not found");
    }

    @Test
    void delete_whenExists_shouldDelete() {
        when(transactionRepository.findById(TRANSACTION_ID))
                .thenReturn(Optional.of(transaction));

        transactionService.delete(TRANSACTION_ID, USER_ID);

        verify(transactionRepository).deleteById(TRANSACTION_ID);
    }
}