package com.interceptly.api.controller;

import com.interceptly.api.dao.CollaborationDao;
import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.dao.UserDao;
import com.interceptly.api.dao.composites.UserIssueComposite;
import com.interceptly.api.dto.CollaborationDto;
import com.interceptly.api.repository.CollaborationRepository;
import com.interceptly.api.repository.PermissionRepository;
import com.interceptly.api.repository.UserRepository;
import com.interceptly.api.util.PermissionUtil;
import com.interceptly.api.util.enums.PermissionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@RequestMapping("/collaborations")
@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class CollaborationController {

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    PermissionUtil permissionUtil;

    @Autowired
    CollaborationRepository collaborationRepository;

    @Autowired
    UserRepository userRepository;


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CollaborationDao postCollaboration(@NotNull JwtAuthenticationToken authenticationToken, @RequestBody @Valid CollaborationDto collaboration) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, collaboration.getProjectId());
        permissionUtil.checkPermission(permissionDao, PermissionEnum.DEVELOPER);
        Optional<UserDao> userDao = userRepository.findByEmail(collaboration.getEmail());
        if (userDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, null);
        }
        Optional<CollaborationDao> collaborationDao = collaborationRepository.findByUserIdAndIssueId(userDao.get().getId(), collaboration.getIssueId());
        if (collaborationDao.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, null);
        }
        if (Objects.equals(userDao.get().getId(), permissionDao.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null);
        }

        CollaborationDao collaborationDaoNew = CollaborationDao.builder().id(new UserIssueComposite(userDao.get().getId(), collaboration.getIssueId())).createdBy(permissionDao.getUserId()).build();
        collaborationRepository.saveAndFlush(collaborationDaoNew);

        return collaborationDaoNew;
    }

    @DeleteMapping
    public String deleteCollaboration(@NotNull JwtAuthenticationToken authenticationToken, @RequestBody @Valid CollaborationDto collaboration) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, collaboration.getProjectId());
        permissionUtil.checkPermission(permissionDao, PermissionEnum.DEVELOPER);
        Optional<CollaborationDao> collaborationDao = collaborationRepository.findByUserIdAndIssueId(collaboration.getUserId(), collaboration.getIssueId());
        if (collaborationDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, null);
        }
        collaborationRepository.delete(collaborationDao.get());
        return "Collaboration deleted.";
    }
}
