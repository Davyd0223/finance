package com.javaapp.finance.web;

import com.javaapp.finance.dto.ProfileTo;
import com.javaapp.finance.model.User;
import com.javaapp.finance.security.AuthUser;
import com.javaapp.finance.service.UserService;
import com.javaapp.finance.web.mapper.ProfileMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profile", description = "Профиль пользователя")
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @Operation(summary = "Получить профиль текущего пользователя")
    @GetMapping
    public ProfileTo get(@AuthenticationPrincipal AuthUser authUser) {
        User user = userService.getById(authUser.getId());
        return ProfileMapper.toDto(user);
    }

    @Operation(summary = "Обновить профиль")
    @PutMapping
    public ProfileTo update(@Valid @RequestBody ProfileTo profileTo,
                            @AuthenticationPrincipal AuthUser authUser) {
        userService.updateProfile(
                authUser.getId(),
                profileTo.getName(),
                profileTo.getDefaultMonthlyBudget()
        );
        User updated = userService.getById(authUser.getId());
        return ProfileMapper.toDto(updated);
    }
}