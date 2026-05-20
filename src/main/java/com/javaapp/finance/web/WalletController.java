package com.javaapp.finance.web;

import com.javaapp.finance.dto.WalletTo;
import com.javaapp.finance.model.Wallet;
import com.javaapp.finance.security.AuthUser;
import com.javaapp.finance.service.WalletService;
import com.javaapp.finance.web.mapper.WalletMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wallets", description = "Управление кошельками")
@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "Получить все кошельки пользователя")
    @GetMapping
    public List<WalletTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        return walletService.getAllByUserId(authUser.getId()).stream()
                .map(WalletMapper::toDto)
                .toList();
    }

    @Operation(summary = "Получить кошелек по id")
    @GetMapping("/{id}")
    public WalletTo getById(@PathVariable Integer id,
                            @AuthenticationPrincipal AuthUser authUser) {
        return WalletMapper.toDto(walletService.getByIdAndUserId(id, authUser.getId()));
    }

    @Operation(summary = "Создать кошелёк")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletTo create(@Valid @RequestBody WalletTo walletTo,
                           @AuthenticationPrincipal AuthUser authUser) {
        Wallet wallet = WalletMapper.fromDto(walletTo, authUser.getUser());
        Wallet created = walletService.create(wallet, authUser.getId());
        return WalletMapper.toDto(created);
    }

    @Operation(summary = "Обновить кошелёк")
    @PutMapping("/{id}")
    public WalletTo update(@PathVariable Integer id,
                           @Valid @RequestBody WalletTo walletTo,
                           @AuthenticationPrincipal AuthUser authUser) {
        Wallet wallet = WalletMapper.fromDto(walletTo, authUser.getUser());
        wallet.setId(id);
        Wallet updated = walletService.update(wallet, authUser.getId());
        return WalletMapper.toDto(updated);
    }

    @Operation(summary = "Удалить кошелёк")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id,
                       @AuthenticationPrincipal AuthUser authUser) {
        walletService.delete(id, authUser.getId());
    }
}