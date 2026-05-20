package com.javaapp.finance.dto;

import com.javaapp.finance.model.Category;
import com.javaapp.finance.model.OperationKind;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTo {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull
    private Category category;

    @NotNull
    private OperationKind kind;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private Integer walletId;
}
