package com.javaapp.finance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTo {

    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    @NotBlank
    @Email
    private String email;

    @Size(min = 5)
    private String password;

    private String walletName;
}
