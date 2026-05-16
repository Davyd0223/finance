package com.javaApp.finance.service;

import com.javaApp.finance.model.Wallet;
import com.javaApp.finance.repository.WalletRepository;
import com.javaApp.finance.util.Messages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final Messages messages;

    @Transactional(readOnly = true)
    public List<Wallet> getAllByUserId(Integer userId) {
        return walletRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Wallet getByIdAndUserId(Integer id, Integer userId) {
        return walletRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NoSuchElementException(messages.get("error.wallet.notFound")));
    }

    @Transactional
    public Wallet create(Wallet wallet, Integer userId) {
        if (wallet.getId() != null) {
            throw new IllegalArgumentException(messages.get("error.id.mustBeNull"));
        }
        checkUser(wallet, userId);
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet update(Wallet wallet, Integer userId) {
        getByIdAndUserId(wallet.getId(), userId);
        checkUser(wallet, userId);
        return walletRepository.save(wallet);
    }

    @Transactional
    public void delete(Integer id, Integer userId) {
        getByIdAndUserId(id, userId);
        walletRepository.deleteById(id);
    }

    public boolean existsByUserIdAndName(Integer userId, String name) {
        return walletRepository.existsByUserIdAndName(userId, name);
    }

    private void checkUser(Wallet wallet, Integer userId) {
        if (!wallet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(messages.get("error.wallet.notOwner"));
        }
    }
}
