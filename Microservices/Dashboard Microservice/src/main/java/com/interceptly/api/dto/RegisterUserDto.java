package com.interceptly.api.dto;

import com.interceptly.api.util.ProviderEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterUserDto {
    private String firstName;
    private String lastName;
    @NotEmpty(message = "The email address is required.")
    @Email(message = "The email address is invalid.")
    private String email;
    private String password;
    private ProviderEnum provider;
}
