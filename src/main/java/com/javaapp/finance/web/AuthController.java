package com.javaapp.finance.web;

import com.javaapp.finance.dto.UserTo;
import com.javaapp.finance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Регистрация")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserTo userTo) {
        String walletName = userTo.getWalletName();
        if (walletName == null || walletName.isBlank()) {
            walletName = "Main Wallet";
        }
        userService.registerUser(
                userTo.getName(),
                userTo.getEmail(),
                userTo.getPassword(),
                walletName
        );
    }
}
