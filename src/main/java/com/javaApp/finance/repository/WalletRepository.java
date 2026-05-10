package com.javaApp.finance.repository;

import com.javaApp.finance.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findFirstByUserIdOrderByIdAsc(Integer userId);

    List<Wallet> findAllByUserIdOrderByIdAsc(Integer userId);
}
