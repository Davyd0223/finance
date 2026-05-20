package com.javaapp.finance.repository;

import com.javaapp.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByUser_IdOrderByDateTimeDesc(Integer userId);

    List<Transaction> findAllByUser_IdAndWallet_IdOrderByDateTimeDesc(Integer userId, Integer walletId);
}