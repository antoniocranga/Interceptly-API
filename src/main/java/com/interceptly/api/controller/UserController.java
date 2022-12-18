package com.interceptly.api.controller;

import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.dao.ProjectDao;
import com.interceptly.api.dao.UserDao;
import com.interceptly.api.repository.PermissionRepository;
import com.interceptly.api.repository.ProjectRepository;
import com.interceptly.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @GetMapping
    public Optional<UserDao> getUser(@NotNull JwtAuthenticationToken authentication) {
        Integer userId = Integer.parseInt(authentication.getTokenAttributes().get("user_id").toString());
        return userRepository.findById(userId);
    }
}
