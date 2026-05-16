package com.javaApp.finance.service;

import com.javaApp.finance.model.User;
import com.javaApp.finance.model.Wallet;
import com.javaApp.finance.repository.WalletRepository;
import com.javaApp.finance.util.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.javaApp.finance.TestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private Messages messages;
    @InjectMocks
    private WalletService walletService;

    private User user;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        user = createUser();
        wallet = createWallet(user);
    }

    @Test
    void getByIdAndUserId_whenExists_shouldReturn() {
        when(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID)).thenReturn(Optional.of(wallet));

        assertThat(walletService.getByIdAndUserId(WALLET_ID, USER_ID)).isEqualTo(wallet);
    }

    @Test
    void getByIdAndUserId_whenNotFound_shouldThrow() {
        when(walletRepository.findByIdAndUserId(999, USER_ID)).thenReturn(Optional.empty());
        when(messages.get("error.wallet.notFound")).thenReturn("Не найден");

        assertThatThrownBy(() -> walletService.getByIdAndUserId(999, USER_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void create_whenValid_shouldSave() {
        wallet.setId(null);
        when(walletRepository.save(wallet)).thenReturn(wallet);

        assertThat(walletService.create(wallet, USER_ID)).isEqualTo(wallet);
    }
}