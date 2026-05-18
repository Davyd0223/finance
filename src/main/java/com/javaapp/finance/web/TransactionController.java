package com.javaapp.finance.web;

import com.javaapp.finance.model.*;
import com.javaapp.finance.service.CurrentUserService;
import com.javaapp.finance.service.TransactionService;
import com.javaapp.finance.service.WalletService;
import com.javaapp.finance.util.Messages;
import com.javaapp.finance.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final WalletService walletService;
    private final Messages messages;

    @PostMapping("/transactions")
    public String create(
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("category") Category category,
            @RequestParam("kind") OperationKind kind,
            @RequestParam("date_time") String dateTime,
            @RequestParam("wallet_id") Integer walletId,
            RedirectAttributes redirectAttributes
    ) {
        ValidationUtil validation = new ValidationUtil()
                .requirePositive(amount, messages.get("validation.amount.positive"))
                .requireDigits(amount, 13, 2, messages.get("validation.amount.format"))
                .requireNotBlank(dateTime, messages.get("validation.date.blank"))
                .requireNotNull(walletId, messages.get("validation.wallet.select"));

        if (validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
            return "redirect:/app?walletId=" + (walletId != null ? walletId : 0);
        }

        Integer userId = CurrentUserService.getCurrentUserId();
        User user = CurrentUserService.getCurrentUser();
        Wallet wallet = walletService.getByIdAndUserId(walletId, userId);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setKind(kind);
        transaction.setDateTime(LocalDateTime.parse(dateTime));

        transactionService.create(transaction, userId);
        return "redirect:/app?walletId=" + walletId;
    }

    @GetMapping("/transactions/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        Integer userId = CurrentUserService.getCurrentUserId();

        Transaction transaction = transactionService.getByIdAndUserId(id, userId);
        List<Wallet> wallets = walletService.getAllByUserId(userId);

        model.addAttribute("transaction", transaction);
        model.addAttribute("wallets", wallets);
        model.addAttribute("categories", Category.values());
        model.addAttribute("kinds", OperationKind.values());

        return "transaction-edit";
    }

    @PostMapping("/transactions/{id}/edit")
    public String edit(
            @PathVariable Integer id,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("category") Category category,
            @RequestParam("kind") OperationKind kind,
            @RequestParam("date_time") String dateTime,
            @RequestParam("wallet_id") Integer walletId,
            RedirectAttributes redirectAttributes
    ) {
        ValidationUtil validation = new ValidationUtil()
                .requirePositive(amount, messages.get("validation.amount.positive"))
                .requireNotBlank(dateTime, messages.get("validation.date.blank"))
                .requireNotNull(walletId, messages.get("validation.wallet.select"));

        if (validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
            return "redirect:/transactions/" + id + "/edit";
        }

        Integer userId = CurrentUserService.getCurrentUserId();
        User user = CurrentUserService.getCurrentUser();
        Wallet wallet = walletService.getByIdAndUserId(walletId, userId);

        Transaction transaction = transactionService.getByIdAndUserId(id, userId);
        transaction.setUser(user);
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setKind(kind);
        transaction.setDateTime(LocalDateTime.parse(dateTime));

        transactionService.update(transaction, userId);
        return "redirect:/app?walletId=" + walletId;
    }

    @PostMapping("/transactions/{id}/delete")
    public String delete(@PathVariable Integer id,
                         @RequestParam(value = "walletId", required = false) Integer walletId,
                         @RequestParam(value = "returnTo", required = false) String returnTo) {
        Integer userId = CurrentUserService.getCurrentUserId();
        transactionService.delete(id, userId);

        if ("list".equals(returnTo)) {
            return "redirect:/list";
        }

        if (walletId != null && !walletId.equals(0)) {
            return "redirect:/app?walletId=" + walletId;
        }
        return "redirect:/app";
    }
}
