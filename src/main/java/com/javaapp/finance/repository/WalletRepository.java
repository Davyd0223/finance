package com.javaapp.finance.repository;

import com.javaapp.finance.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    List<Wallet> findAllByUser_Id(Integer userId);
}