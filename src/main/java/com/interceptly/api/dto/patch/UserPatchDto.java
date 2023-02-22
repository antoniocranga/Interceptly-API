package com.interceptly.api.dto.patch;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserPatchDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;
}
