package com.javaapp.finance.web.mapper;

import com.javaapp.finance.dto.TransactionTo;
import com.javaapp.finance.model.Transaction;
import com.javaapp.finance.model.User;
import com.javaapp.finance.model.Wallet;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionMapper {

    public static TransactionTo toDto(Transaction transaction) {
        return new TransactionTo(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getKind(),
                transaction.getDateTime(),
                transaction.getWallet().getId()
        );
    }

    public static Transaction fromDto(TransactionTo transactionTo, User user) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionTo.getId());
        transaction.setAmount(transactionTo.getAmount());
        transaction.setCategory(transactionTo.getCategory());
        transaction.setKind(transactionTo.getKind());
        transaction.setDateTime(transactionTo.getDateTime());
        transaction.setUser(user);

        Wallet wallet = new Wallet();
        wallet.setId(transactionTo.getWalletId());
        transaction.setWallet(wallet);

        return transaction;
    }
}