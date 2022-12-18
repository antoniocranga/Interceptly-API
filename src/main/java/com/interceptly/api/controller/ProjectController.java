package com.interceptly.api.controller;

import com.interceptly.api.dao.EventDao;
import com.interceptly.api.dao.IssueDao;
import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.dao.ProjectDao;
import com.interceptly.api.dao.composites.UserProjectComposite;
import com.interceptly.api.dto.ProjectDto;
import com.interceptly.api.repository.EventRepository;
import com.interceptly.api.repository.IssueRepository;
import com.interceptly.api.repository.PermissionRepository;
import com.interceptly.api.repository.ProjectRepository;
import com.interceptly.api.util.enums.PermissionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;


@RequestMapping("/projects")
@RestController
@Slf4j
public class ProjectController {

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    ProjectRepository projectRepository;

    @GetMapping
    public List<PermissionDao> getProjects(@NotNull JwtAuthenticationToken authenticationToken) {
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        return permissionRepository.findAllByUserId(userId);
    }

    @PostMapping
    public PermissionDao postProject(@NotNull JwtAuthenticationToken authenticationToken, @NotNull @Valid @RequestBody ProjectDto project) {
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        ProjectDao projectDao = projectRepository.saveAndFlush(project.toProjectDao(userId));
        PermissionDao permissionDao = PermissionDao.builder().id(new UserProjectComposite(userId, projectDao.getId())).project(projectDao).permission(PermissionEnum.OWNER).build();
        log.debug(permissionDao.toString());
        return permissionRepository.saveAndFlush(permissionDao);
    }

    @GetMapping("/{projectId}/issues")
    public List<IssueDao> getIssues(@NotNull JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId, @PathParam("limit") Integer limit, @PathParam("page") Integer page) {
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        Optional<PermissionDao> permissionDao = permissionRepository.findByUserIdAndProjectId(userId, projectId);
        if (permissionDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        return issueRepository.findAll();
    }

    @GetMapping("/{projectId}/issues/{issueId}")
    public Optional<IssueDao> getIssue(@NotNull JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId, @PathVariable Integer issueId, @PathParam("limit") Integer limit, @PathParam("page") Integer page) {
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        Optional<PermissionDao> permissionDao = permissionRepository.findByUserIdAndProjectId(userId, projectId);
        if (permissionDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        List<EventDao> events = eventRepository.findAllByIssueId(issueId);
        Optional<IssueDao> issueDao = issueRepository.findById(issueId);
        if (issueDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        issueDao.ifPresent(issue -> issue.setEvents(events));
        return issueDao;
    }
}
