package com.javaapp.finance.web;

import com.javaapp.finance.service.UserService;
import com.javaapp.finance.util.Messages;
import com.javaapp.finance.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@AllArgsConstructor
@Controller
public class AuthController {
    private UserService userService;
    private final Messages messages;

    @PostMapping("/auth/register")
    public String register(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("walletName") String walletName,
            RedirectAttributes redirectAttributes
    ) {
        ValidationUtil validation = new ValidationUtil()
                .requireNotBlank(name, messages.get("validation.name.blank"))
                .requireMinLength(name, 2, messages.get("validation.name.min"))
                .requireMaxLength(name, 100, messages.get("validation.name.max"))
                .requireNotBlank(email, messages.get("validation.email.blank"))
                .requireMinLength(password, 5, messages.get("validation.password.min"))
                .requireNotBlank(walletName, messages.get("validation.wallet.name.blank"))
                .requireMinLength(walletName, 2, messages.get("validation.wallet.name.min"))
                .requireMaxLength(walletName, 20, messages.get("validation.wallet.name.max"));

        if (validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerErrors", validation.getErrors());
            return "redirect:/?registerError";
        }

        try {
            userService.registerUser(name, email, password, walletName);
            return "redirect:/?registered";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("registerErrors", List.of(e.getMessage()));
            return "redirect:/?registerError";
        }
    }
}

