package com.javaApp.finance.repository;

import com.javaApp.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByUserIdOrderByDateTimeDesc(Integer userId);

    Optional<Transaction> findByIdAndUserId(Integer id, Integer userId);
}
