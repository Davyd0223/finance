package com.javaApp.finance.web;

import com.javaApp.finance.model.*;
import com.javaApp.finance.service.CurrentUserService;
import com.javaApp.finance.service.TransactionService;
import com.javaApp.finance.service.UserService;
import com.javaApp.finance.service.WalletService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class RootController {

    private final TransactionService transactionService;
    private final WalletService walletService;
    private final UserService userService;

    public RootController(TransactionService transactionService,
                          WalletService walletService,
                          UserService userService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/app")
    public String app(@RequestParam(value = "walletId", required = false) Integer walletId,
                      Model model) {
        Integer userId = CurrentUserService.getCurrentUserId();
        User user = userService.getById(userId);
        List<Wallet> wallets = walletService.getAllByUserId(userId);

        if (wallets.isEmpty()) {
            return "redirect:/list";
        }

        Wallet selectedWallet;
        if (walletId == null || walletId == 0) {
            selectedWallet = wallets.getFirst();
        } else {
            selectedWallet = walletService.getByIdAndUserId(walletId, userId);
        }

        Integer selectedWalletId = selectedWallet.getId();

        List<Transaction> transactions = transactionService.getAllByUserIdAndWalletId(userId, selectedWalletId);
        BigDecimal balance = transactionService.calculateBalanceByWallet(userId, selectedWalletId);
        BigDecimal expenses = transactionService.calculateExpensesByWallet(userId, selectedWalletId);

        model.addAttribute("transactions", transactions);
        model.addAttribute("wallets", wallets);
        model.addAttribute("balance", balance);
        model.addAttribute("expenses", expenses);
        model.addAttribute("budget", user.getDefaultMonthlyBudget());
        model.addAttribute("categories", Category.values());
        model.addAttribute("kinds", OperationKind.values());
        model.addAttribute("selectedWalletId", selectedWalletId);
        model.addAttribute("selectedWalletCurrency", selectedWallet.getCurrency());
        model.addAttribute("userCurrency", user.getDefaultCurrency());

        return "app";
    }

    @GetMapping("/list")
    public String list(Model model) {
        int userId = CurrentUserService.getCurrentUserId();
        User user = userService.getById(userId);

        List<Transaction> transactions = transactionService.getAllByUserId(userId);
        List<Wallet> wallets = walletService.getAllByUserId(userId);

        model.addAttribute("user", user);
        model.addAttribute("transactions", transactions);
        model.addAttribute("wallets", wallets);
        model.addAttribute("userCurrency", user.getDefaultCurrency());
        model.addAttribute("currencies", Currency.values());
        model.addAttribute("walletTypes", WalletType.values());

        return "list";
    }
}
