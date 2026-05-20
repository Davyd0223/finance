package com.javaapp.finance.service;

import com.javaapp.finance.model.Currency;
import com.javaapp.finance.model.User;
import com.javaapp.finance.model.Wallet;
import com.javaapp.finance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static com.javaapp.finance.TestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletService walletService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @Test
    void registerUser_whenValid_shouldSaveAndCreateWallet() {
        when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(USER_PASSWORD)).thenReturn(USER_ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(walletService.create(any(Wallet.class), any())).thenReturn(new Wallet());

        userService.registerUser(USER_NAME, USER_EMAIL, USER_PASSWORD, WALLET_NAME);

        verify(userRepository).save(any(User.class));
        verify(walletService).create(any(Wallet.class), any());
    }

    @Test
    void registerUser_whenEmailExists_shouldThrow() {
        when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(true);

        assertThatThrownBy(() ->
                userService.registerUser(USER_NAME, USER_EMAIL, USER_PASSWORD, WALLET_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already exists");
    }

    @Test
    void registerUser_shouldSetDefaultCurrencyUSD() {
        when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(USER_PASSWORD)).thenReturn(USER_ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(walletService.create(any(Wallet.class), any())).thenReturn(new Wallet());

        userService.registerUser(USER_NAME, USER_EMAIL, USER_PASSWORD, WALLET_NAME);

        verify(userRepository).save(argThat(u ->
                u.getDefaultCurrency() == Currency.USD &&
                        u.getDefaultMonthlyBudget().compareTo(BigDecimal.ZERO) == 0
        ));
    }

    @Test
    void getById_whenExists_shouldReturnUser() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        assertThat(userService.getById(USER_ID)).isEqualTo(user);
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(999))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User not found");
    }

    @Test
    void updateProfile_whenValid_shouldUpdateNameAndBudget() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateProfile(USER_ID, "Петр", new BigDecimal("5000.00"));

        verify(userRepository).save(argThat(u ->
                u.getName().equals("Петр") &&
                        u.getDefaultMonthlyBudget().compareTo(new BigDecimal("5000.00")) == 0
        ));
    }

    @Test
    void updateProfile_whenNotFound_shouldThrow() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                userService.updateProfile(999, "Петр", BigDecimal.ZERO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User not found");

        verify(userRepository, never()).save(any());
    }
}