package com.interceptly.api.controller;

import com.interceptly.api.dao.EventDao;
import com.interceptly.api.dao.IssueDao;
import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.repository.EventRepository;
import com.interceptly.api.repository.IssueRepository;
import com.interceptly.api.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    @GetMapping
    public List<PermissionDao> getProjects(JwtAuthenticationToken authenticationToken){
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        return permissionRepository.findAllByUserId(userId);
    }

    @GetMapping("/{projectId}/issues")
    public List<IssueDao> getIssues(JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId, @PathParam("limit") Integer limit, @PathParam("page") Integer page){
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        Optional<PermissionDao> permissionDao = permissionRepository.findByUserIdAndProjectId(userId,projectId);
        if(permissionDao.isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        return issueRepository.findAll();
    }

    @GetMapping("/{projectId}/issues/{issueId}")
    public Optional<IssueDao> getIssue(JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId, @PathVariable Integer issueId, @PathParam("limit") Integer limit, @PathParam("page") Integer page){
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        Optional<PermissionDao> permissionDao = permissionRepository.findByUserIdAndProjectId(userId,projectId);
        if(permissionDao.isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        List<EventDao> events = eventRepository.findAllByIssueId(issueId);
        Optional<IssueDao> issueDao = issueRepository.findById(issueId);
        if(issueDao.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        issueDao.ifPresent(issue -> issue.setEvents(events));
        return issueDao;
    }
}
