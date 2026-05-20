package com.javaapp.finance.dto;

import com.javaapp.finance.model.Currency;
import com.javaapp.finance.model.WalletType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTo {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    @NotNull
    private Currency currency;

    @NotNull
    private WalletType type;
}
