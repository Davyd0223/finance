package com.javaapp.finance.service;

import com.javaapp.finance.model.Wallet;
import com.javaapp.finance.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Cacheable(value = "wallets", key = "#userId")
    @Transactional(readOnly = true)
    public List<Wallet> getAllByUserId(Integer userId) {
        return walletRepository.findAllByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public Wallet getByIdAndUserId(Integer id, Integer userId) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Wallet not found"));

        if (!wallet.getUser().getId().equals(userId)) {
            throw new NoSuchElementException("Wallet not found");
        }

        return wallet;
    }

    @CacheEvict(value = "wallets", key = "#userId")
    @Transactional
    public Wallet create(Wallet wallet, Integer userId) {
        if (wallet.getId() != null) {
            throw new IllegalArgumentException("Id must be null");
        }
        checkUser(wallet, userId);
        return walletRepository.save(wallet);
    }

    @CacheEvict(value = "wallets", key = "#userId")
    @Transactional
    public Wallet update(Wallet wallet, Integer userId) {
        getByIdAndUserId(wallet.getId(), userId);
        checkUser(wallet, userId);
        return walletRepository.save(wallet);
    }

    @CacheEvict(value = "wallets", key = "#userId")
    @Transactional
    public void delete(Integer id, Integer userId) {
        getByIdAndUserId(id, userId);
        walletRepository.deleteById(id);
    }

    private void checkUser(Wallet wallet, Integer userId) {
        if (!wallet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Wallet does not belong to user");
        }
    }
}