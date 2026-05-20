package com.javaapp.finance.web;

import com.javaapp.finance.dto.ProfileTo;
import com.javaapp.finance.model.User;
import com.javaapp.finance.repository.UserRepository;
import com.javaapp.finance.web.mapper.ProfileMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Admin\", description = \"Администрирование пользователей")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public List<ProfileTo> getAll() {
        return userRepository.findAll().stream()
                .map(ProfileMapper::toDto)
                .toList();
    }

    @Operation(summary = "Получить пользователя по id")
    @GetMapping("/{id}")
    public ProfileTo getById(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return ProfileMapper.toDto(user);
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found");
        }
        userRepository.deleteById(id);
    }
}
