package com.javaApp.finance.web;

import com.javaApp.finance.model.Currency;
import com.javaApp.finance.model.User;
import com.javaApp.finance.model.Wallet;
import com.javaApp.finance.model.WalletType;
import com.javaApp.finance.service.CurrentUserService;
import com.javaApp.finance.service.WalletService;
import com.javaApp.finance.util.Messages;
import com.javaApp.finance.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@AllArgsConstructor
@Controller
public class WalletController {

    private final WalletService walletService;
    private final Messages messages;

    @PostMapping("/wallets")
    public String create(
            @RequestParam("name") String name,
            @RequestParam("currency") Currency currency,
            @RequestParam("type") WalletType type,
            RedirectAttributes redirectAttributes
    ) {
        User user = CurrentUserService.getCurrentUser();
        Integer userId = CurrentUserService.getCurrentUserId();

        ValidationUtil validation = new ValidationUtil()
                .requireNotBlank(name, messages.get("validation.wallet.name.blank"))
                .requireMinLength(name, 2, messages.get("validation.wallet.name.min"))
                .requireMaxLength(name, 20, messages.get("validation.wallet.name.max"));

        if (validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
            return "redirect:/list";
        }

        if (walletService.existsByUserIdAndName(userId, name)) {
            redirectAttributes.addFlashAttribute("errors",
                    List.of(messages.get("validation.wallet.name.exists")));
            return "redirect:/list";
        }

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setName(name);
        wallet.setCurrency(currency);
        wallet.setType(type);

        walletService.create(wallet, userId);
        redirectAttributes.addFlashAttribute("successMessage", "Кошелек создан");
        return "redirect:/list";
    }

    @PostMapping("/wallets/{id}/delete")
    public String delete(@PathVariable Integer id) {
        Integer userId = CurrentUserService.getCurrentUserId();
        walletService.delete(id, userId);
        return "redirect:/list";
    }
}
