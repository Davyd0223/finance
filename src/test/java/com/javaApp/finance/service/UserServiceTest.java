package com.javaApp.finance.service;

import com.javaApp.finance.model.User;
import com.javaApp.finance.model.Wallet;
import com.javaApp.finance.repository.UserRepository;
import com.javaApp.finance.util.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.javaApp.finance.TestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletService walletService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Messages messages;
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
        when(messages.get("error.user.exists")).thenReturn("Уже существует");

        assertThatThrownBy(() ->
                userService.registerUser(USER_NAME, USER_EMAIL, USER_PASSWORD, WALLET_NAME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        when(messages.get("error.user.notFound")).thenReturn("Не найден");

        assertThatThrownBy(() -> userService.getById(999))
                .isInstanceOf(NoSuchElementException.class);
    }
}