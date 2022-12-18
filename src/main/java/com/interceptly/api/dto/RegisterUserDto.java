package com.interceptly.api.dto;

import com.interceptly.api.util.ProviderEnum;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class RegisterUserDto {
    private String firstName;
    private String lastName;
    @NotEmpty(message = "The email address is required.")
    @Email(message= "The email address is invalid.")
    private String email;
    private String password;
    private ProviderEnum provider;
}
