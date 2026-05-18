package com.javaapp.finance.repository;

import com.javaapp.finance.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    List<Wallet> findAllByUserId(Integer userId);

    Optional<Wallet> findByIdAndUserId(Integer id, Integer userId);

    boolean existsByUserIdAndName(Integer userId, String name);
}
