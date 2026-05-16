package com.javaApp.finance.service;

import com.javaApp.finance.model.OperationKind;
import com.javaApp.finance.model.Transaction;
import com.javaApp.finance.repository.TransactionRepository;
import com.javaApp.finance.repository.WalletRepository;
import com.javaApp.finance.util.Messages;
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
    private final Messages messages;

    @Transactional(readOnly = true)
    public List<Transaction> getAllByUserId(Integer userId) {
        return transactionRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllByUserIdAndWalletId(Integer userId, Integer walletId) {
        return transactionRepository.findAllByUserIdAndWalletIdOrderByDateTimeDesc(userId, walletId);
    }

    @Transactional(readOnly = true)
    public Transaction getByIdAndUserId(Integer id, Integer userId) {
        return transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NoSuchElementException(messages.get("error.transaction.notFound")));
    }

    @Transactional
    public Transaction create(Transaction transaction, Integer userId) {
        if (transaction.getId() != null) {
            throw new IllegalArgumentException(messages.get("error.id.mustBeNull"));
        }
        validateOwnership(transaction, userId);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction update(Transaction transaction, Integer userId) {
        getByIdAndUserId(transaction.getId(), userId);
        validateOwnership(transaction, userId);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void delete(Integer id, Integer userId) {
        getByIdAndUserId(id, userId);
        transactionRepository.deleteById(id);
    }

    public BigDecimal calculateBalance(Integer userId) {
        return getAllByUserId(userId).stream()
                .map(t -> t.getKind() == OperationKind.INCOME
                        ? t.getAmount()
                        : t.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateBalanceByWallet(Integer userId, Integer walletId) {
        return getAllByUserIdAndWalletId(userId, walletId).stream()
                .map(t -> t.getKind() == OperationKind.INCOME
                        ? t.getAmount()
                        : t.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateExpenses(Integer userId) {
        return getAllByUserId(userId).stream()
                .filter(t -> t.getKind() == OperationKind.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateExpensesByWallet(Integer userId, Integer walletId) {
        return getAllByUserIdAndWalletId(userId, walletId).stream()
                .filter(t -> t.getKind() == OperationKind.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateOwnership(Transaction transaction, Integer userId) {
        if (!transaction.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(messages.get("error.transaction.notOwner"));
        }
        walletRepository.findByIdAndUserId(transaction.getWallet().getId(), userId)
                .orElseThrow(() -> new IllegalArgumentException(messages.get("error.transaction.walletNotOwner")));
    }
}
