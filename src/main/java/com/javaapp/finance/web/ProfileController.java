package com.javaapp.finance.web;

import com.javaapp.finance.model.User;
import com.javaapp.finance.security.AuthUser;
import com.javaapp.finance.service.CurrentUserService;
import com.javaapp.finance.service.UserService;
import com.javaapp.finance.util.Messages;
import com.javaapp.finance.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@AllArgsConstructor
@Controller
public class ProfileController {

    private final UserService userService;
    private final Messages messages;

    @GetMapping("/profile")
    public String profileForm(Model model) {
        int userId = CurrentUserService.getCurrentUserId();
        User user = userService.getById(userId);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("defaultMonthlyBudget") BigDecimal defaultMonthlyBudget,
            RedirectAttributes redirectAttributes
    ) {
        ValidationUtil validation = new ValidationUtil()
                .requireNotBlank(name, messages.get("validation.name.blank"))
                .requireMinLength(name, 2, messages.get("validation.name.min"))
                .requireMaxLength(name, 20, messages.get("validation.name.max"))
                .requireNotNegative(defaultMonthlyBudget, messages.get("validation.budget.negative"))
                .requireDigits(defaultMonthlyBudget, 13, 2, messages.get("validation.budget.format"));

        if (validation.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", validation.getErrors());
            return "redirect:/profile";
        }

        Integer userId = CurrentUserService.getCurrentUserId();
        userService.updateProfile(userId, name, defaultMonthlyBudget);

        AuthUser authUser = CurrentUserService.getCurrentAuthUser();
        authUser.getUser().setName(name);
        authUser.getUser().setDefaultMonthlyBudget(defaultMonthlyBudget);

        redirectAttributes.addFlashAttribute("successMessage", "Профиль обновлен");
        return "redirect:/profile";
    }
}
