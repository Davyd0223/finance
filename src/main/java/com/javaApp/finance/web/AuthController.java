package com.javaApp.finance.web;

import com.javaApp.finance.model.Currency;
import com.javaApp.finance.model.User;
import com.javaApp.finance.model.Wallet;
import com.javaApp.finance.model.WalletType;
import com.javaApp.finance.repository.UserRepository;
import com.javaApp.finance.repository.WalletRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class AuthController {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/register")
    public String register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        if (userRepository.findByEmail(email).isPresent()) {
            return "redirect:/?registerError";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setDefaultCurrency(Currency.USD);
        user.setDefaultMonthlyBudget(BigDecimal.ZERO);
        user = userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setName("main");
        wallet.setCurrency(user.getDefaultCurrency());
        wallet.setType(WalletType.CASH);
        walletRepository.save(wallet);

        return "redirect:/?registered";
    }
}

