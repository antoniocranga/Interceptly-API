package com.interceptly.api.dto.patch;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPatchDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;
}
