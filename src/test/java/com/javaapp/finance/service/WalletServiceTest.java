package com.javaapp.finance.service;

import com.javaapp.finance.model.User;
import com.javaapp.finance.model.Wallet;
import com.javaapp.finance.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.javaapp.finance.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

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
    void getAll_shouldReturnWallets() {
        when(walletRepository.findAllByUser_Id(USER_ID))
                .thenReturn(List.of(wallet));

        assertThat(walletService.getAllByUserId(USER_ID)).hasSize(1);
    }

    @Test
    void getByIdAndUserId_whenExists_shouldReturn() {
        when(walletRepository.findById(WALLET_ID))
                .thenReturn(Optional.of(wallet));

        assertThat(walletService.getByIdAndUserId(WALLET_ID, USER_ID))
                .isEqualTo(wallet);
    }

    @Test
    void getByIdAndUserId_whenNotFound_shouldThrow() {
        when(walletRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> walletService.getByIdAndUserId(999, USER_ID))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Wallet not found");
    }

    @Test
    void create_whenValid_shouldSave() {
        wallet.setId(null);
        when(walletRepository.save(wallet)).thenReturn(wallet);

        assertThat(walletService.create(wallet, USER_ID)).isEqualTo(wallet);
        verify(walletRepository).save(wallet);
    }

    @Test
    void create_whenIdNotNull_shouldThrow() {
        assertThatThrownBy(() -> walletService.create(wallet, USER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id must be null");
    }

    @Test
    void create_whenWrongOwner_shouldThrow() {
        wallet.setId(null);
        User otherUser = createUser(999, "Other", "other@test.com");
        wallet.setUser(otherUser);

        assertThatThrownBy(() -> walletService.create(wallet, USER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wallet does not belong to user");
    }

    @Test
    void delete_whenExists_shouldDelete() {
        when(walletRepository.findById(WALLET_ID))
                .thenReturn(Optional.of(wallet));

        walletService.delete(WALLET_ID, USER_ID);

        verify(walletRepository).deleteById(WALLET_ID);
    }
}