package com.interceptly.api.controller;

import java.util.Map;
import java.util.Optional;

import com.interceptly.api.dao.UserDao;
import com.interceptly.api.dto.AuthResultDto;
import com.interceptly.api.repository.UserRepository;
import com.interceptly.api.util.ProviderEnum;
import com.interceptly.api.util.security.CustomUserDetailsService;
import com.interceptly.api.util.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path = "login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public AuthResultDto login(
            @RequestParam String email,
            @RequestParam String password) {

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            Optional<UserDao> userDao = userRepository.findByEmail(email);
            Map<String, String> claims = Map.of("user_id",userDao.get().getId().toString());
            String jwt = jwtHelper.createJwtForClaims(email, claims);
            return new AuthResultDto(
                    Optional.empty(),jwt
            );
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    @PostMapping(path = "register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public AuthResultDto register(@RequestParam String email, @RequestParam String password, @RequestParam Integer provider) {
        UserDao userDao = new UserDao();
        userDao.setEmail(email);
        userDao.setPassword(passwordEncoder.encode(password));
        userDao.setProvider(ProviderEnum.values()[provider]);
        try{
            userDao = userRepository.save(userDao);
            Map<String, String> claims = Map.of("user_id",userDao.getId().toString());
            String jwt = jwtHelper.createJwtForClaims(email,claims);
            return new AuthResultDto(Optional.of(userDao), jwt);
        }catch(Exception ex){
            throw ex;
        }
    }
}
