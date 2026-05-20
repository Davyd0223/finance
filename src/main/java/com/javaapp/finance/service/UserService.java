package com.javaapp.finance.service;

import com.javaapp.finance.model.*;
import com.javaapp.finance.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Transactional
    public void registerUser(String name, String email, String password, String walletName) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setRole(Role.USER);
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

    @Cacheable(value = "users", key ="#userId")
    @Transactional(readOnly = true)
    public User getById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public void updateProfile(Integer userId, String name, BigDecimal defaultMonthlyBudget) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setName(name);
        user.setDefaultMonthlyBudget(defaultMonthlyBudget);
        userRepository.save(user);
    }
}