package com.interceptly.api.dto;

import com.interceptly.api.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@AllArgsConstructor
public class AuthResultDto {

    private Optional<UserDao> userDao;

    @NotNull
    private final String jwt;
}
