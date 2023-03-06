package com.example.authmicroservice.dto;

import com.example.authmicroservice.dao.UserDao;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class AuthResultDto {

    private final String jwt;
    @JsonProperty("user")
    private Optional<UserDao> userDao;
}
