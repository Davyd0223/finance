package com.javaApp.finance.repository;

import com.javaApp.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByUserIdOrderByDateTimeDesc(Integer userId);

    List<Transaction> findAllByUserIdAndWalletIdOrderByDateTimeDesc(Integer userId, Integer walletId);

    Optional<Transaction> findByIdAndUserId(Integer id, Integer userId);
}
