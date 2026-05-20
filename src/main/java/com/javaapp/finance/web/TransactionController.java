package com.javaapp.finance.web;

import com.javaapp.finance.dto.TransactionTo;
import com.javaapp.finance.model.Transaction;
import com.javaapp.finance.security.AuthUser;
import com.javaapp.finance.service.TransactionService;
import com.javaapp.finance.web.mapper.TransactionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Transactions", description = "Управление транзакциями")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Получить транзакции пользователя, можно отфильтровать по walletId")
    @GetMapping
    public List<TransactionTo> getAll(@RequestParam(required = false) Integer walletId,
                                      @AuthenticationPrincipal AuthUser authUser) {
        List<Transaction> transactions = (walletId == null)
                ? transactionService.getAllByUserId(authUser.getId())
                : transactionService.getAllByUserIdAndWalletId(authUser.getId(), walletId);

        return transactions.stream()
                .map(TransactionMapper::toDto)
                .toList();
    }

    @Operation(summary = "Получить транзакцию по id")
    @GetMapping("/{id}")
    public TransactionTo getById(@PathVariable Integer id,
                                 @AuthenticationPrincipal AuthUser authUser) {
        return TransactionMapper.toDto(transactionService.getByIdAndUserId(id, authUser.getId()));
    }

    @Operation(summary = "Создать транзакцию")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionTo create(@Valid @RequestBody TransactionTo transactionTo,
                                @AuthenticationPrincipal AuthUser authUser) {
        Transaction transaction = TransactionMapper.fromDto(transactionTo, authUser.getUser());
        Transaction created = transactionService.create(transaction, authUser.getId());
        return TransactionMapper.toDto(created);
    }

    @Operation(summary = "Обновить транзакцию")
    @PutMapping("/{id}")
    public TransactionTo update(@PathVariable Integer id,
                                @Valid @RequestBody TransactionTo transactionTo,
                                @AuthenticationPrincipal AuthUser authUser) {
        Transaction transaction = TransactionMapper.fromDto(transactionTo, authUser.getUser());
        transaction.setId(id);
        Transaction updated = transactionService.update(transaction, authUser.getId());
        return TransactionMapper.toDto(updated);
    }

    @Operation(summary = "Удалить транзакцию")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id,
                       @AuthenticationPrincipal AuthUser authUser) {
        transactionService.delete(id, authUser.getId());
    }
}