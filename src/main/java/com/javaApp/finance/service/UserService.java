package com.javaApp.finance.service;

import com.javaApp.finance.model.Currency;
import com.javaApp.finance.model.User;
import com.javaApp.finance.model.Wallet;
import com.javaApp.finance.model.WalletType;
import com.javaApp.finance.repository.UserRepository;
import com.javaApp.finance.util.Messages;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final PasswordEncoder passwordEncoder;
    private final Messages messages;

    @Transactional
    public void registerUser(String name, String email, String password, String walletName) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(messages.get("error.user.exists"));
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setDefaultCurrency(Currency.USD);
        user.setDefaultMonthlyBudget(BigDecimal.ZERO);
        userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setName(walletName);
        wallet.setCurrency(Currency.USD);
        wallet.setType(WalletType.CASH);
        walletService.create(wallet, user.getId());
    }

    @Transactional
    public void updateProfile(Integer userId, String name, BigDecimal defaultMonthlyBudget) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(messages.get("error.user.notFound")));
        user.setName(name);
        user.setDefaultMonthlyBudget(defaultMonthlyBudget);
        userRepository.save(user);
    }

    public User getById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(messages.get("error.user.notFound")));
    }
}
