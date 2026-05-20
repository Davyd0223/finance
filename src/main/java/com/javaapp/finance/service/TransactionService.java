package com.javaapp.finance.service;

import com.javaapp.finance.model.OperationKind;
import com.javaapp.finance.model.Transaction;
import com.javaapp.finance.model.Wallet;
import com.javaapp.finance.repository.TransactionRepository;
import com.javaapp.finance.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public Transaction create(Transaction transaction, Integer userId) {
        if (transaction.getId() != null) {
            throw new IllegalArgumentException("Id must be null");
        }
        Wallet wallet = validateOwnershipAndGetWallet(transaction, userId);
        transaction.setWallet(wallet);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction update(Transaction transaction, Integer userId) {
        getByIdAndUserId(transaction.getId(), userId);
        Wallet wallet = validateOwnershipAndGetWallet(transaction, userId);
        transaction.setWallet(wallet);
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllByUserId(Integer userId) {
        return transactionRepository.findAllByUser_IdOrderByDateTimeDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllByUserIdAndWalletId(Integer userId, Integer walletId) {
        return transactionRepository.findAllByUser_IdAndWallet_IdOrderByDateTimeDesc(userId, walletId);
    }

    @Transactional(readOnly = true)
    public Transaction getByIdAndUserId(Integer id, Integer userId) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new NoSuchElementException("Transaction not found");
        }

        return transaction;
    }

    @Transactional
    public void delete(Integer id, Integer userId) {
        getByIdAndUserId(id, userId);
        transactionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateBalance(Integer userId) {
        return getAllByUserId(userId).stream()
                .map(t -> t.getKind() == OperationKind.INCOME
                        ? t.getAmount()
                        : t.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Wallet validateOwnershipAndGetWallet(Transaction transaction, Integer userId) {
        if (!transaction.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Transaction does not belong to user");
        }

        Integer walletId = transaction.getWallet().getId();

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NoSuchElementException("Wallet not found"));

        if (!wallet.getUser().getId().equals(userId)) {
            throw new NoSuchElementException("Wallet not found");
        }

        return wallet;
    }
}