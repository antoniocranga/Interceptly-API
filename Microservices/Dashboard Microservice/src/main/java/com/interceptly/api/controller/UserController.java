package com.interceptly.api.controller;

import com.interceptly.api.dao.UserDao;
import com.interceptly.api.dto.patch.UserPatchDto;
import com.interceptly.api.repository.PermissionRepository;
import com.interceptly.api.repository.ProjectRepository;
import com.interceptly.api.repository.UserRepository;
import com.interceptly.api.util.PermissionUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
@RequestMapping("/users")
@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    PermissionUtil permissionUtil;

    @GetMapping
    public Optional<UserDao> getUser(@NotNull JwtAuthenticationToken authentication) {
        Integer userId = Integer.parseInt(authentication.getTokenAttributes().get("user_id").toString());
        return userRepository.findById(userId);
    }

    @PatchMapping
    public UserDao patchUser(@NotNull JwtAuthenticationToken authentication, @RequestBody @Valid UserPatchDto userPatchDto) {
        Integer userId = Integer.parseInt(authentication.getTokenAttributes().get("user_id").toString());
        Optional<UserDao> userDao = userRepository.findById(userId);
        if (userDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null);
        }
        if (userPatchDto.getUsername() != null) {
            userDao.get().setUsername(userPatchDto.getUsername());
        }
        if (userPatchDto.getLastName() != null) {
            userDao.get().setLastName(userPatchDto.getLastName());

        }
        if (userPatchDto.getFirstName() != null) {
            userDao.get().setFirstName(userPatchDto.getFirstName());
        }
        userRepository.saveAndFlush(userDao.get());
        return userDao.get();
    }
}
