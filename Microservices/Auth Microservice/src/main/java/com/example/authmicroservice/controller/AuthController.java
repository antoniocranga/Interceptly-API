package com.example.authmicroservice.controller;

import com.example.authmicroservice.dao.UserDao;
import com.example.authmicroservice.dto.AuthResultDto;
import com.example.authmicroservice.repository.UserRepository;
import com.example.authmicroservice.util.JwtTokenUtil;
import com.example.authmicroservice.util.enums.ProviderEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtHelper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path = "login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public AuthResultDto login(
            @RequestParam String email,
            @RequestParam String password) {

        Optional<UserDao> userDao = userRepository.findByEmail(email);
        if (userDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        } else {
            if (passwordEncoder.matches(password, userDao.get().getPassword())) {
                Map<String, String> claims = Map.of("user_id", userDao.get().getId().toString());
                String jwt = jwtHelper.createToken(email, claims);
                return new AuthResultDto(
                        jwt, userDao
                );
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    @PostMapping(path = "register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResultDto register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam ProviderEnum provider) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already registered");
        }
        UserDao userDao = UserDao.builder().email(email).password(passwordEncoder.encode(password)).provider(provider).firstName(firstName).lastName(lastName).build();
        userDao = userRepository.save(userDao);
        Map<String, String> claims = Map.of("user_id", userDao.getId().toString());
        String jwt = jwtHelper.createToken(email, claims);
        return new AuthResultDto(jwt, Optional.of(userDao));
    }
}
