package com.interceptly.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.interceptly.api.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@AllArgsConstructor
public class AuthResultDto {

    @JsonProperty("user")
    private Optional<UserDao> userDao;
    @NotNull
    private final String jwt;
}
