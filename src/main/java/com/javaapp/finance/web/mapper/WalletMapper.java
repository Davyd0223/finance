package com.javaapp.finance.web.mapper;

import com.javaapp.finance.dto.WalletTo;
import com.javaapp.finance.model.User;
import com.javaapp.finance.model.Wallet;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WalletMapper {

    public static WalletTo toDto(Wallet wallet){
        return new WalletTo(
                wallet.getId(),
                wallet.getName(),
                wallet.getCurrency(),
                wallet.getType()
        );
    }

    public static Wallet fromDto(WalletTo walletTo, User user){
         Wallet wallet = new Wallet();
         wallet.setId(walletTo.getId());
         wallet.setName(walletTo.getName());
         wallet.setCurrency(walletTo.getCurrency());
         wallet.setType(walletTo.getType());
         wallet.setUser(user);
        return wallet;
    }
}
