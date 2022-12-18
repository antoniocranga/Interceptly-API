package com.interceptly.api.controller;

import com.interceptly.api.dao.IssueDao;
import com.interceptly.api.dao.ProjectDao;
import com.interceptly.api.dto.EventDto;
import com.interceptly.api.repository.EventRepository;
import com.interceptly.api.repository.IssueRepository;
import com.interceptly.api.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.websocket.server.PathParam;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/events")
@RestController
@Slf4j
public class EventController {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    ProjectRepository projectRepository;
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public String postEvent(HttpServletRequest request, @RequestBody @NotNull @Valid EventDto event, @PathParam(value="apiKey") @Size(min = 36, max = 36) UUID apiKey){
        Optional<ProjectDao> projectDao = projectRepository.findByApiKey(apiKey);
        if(projectDao.isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        event.setRequest(request);
        Optional<IssueDao> issueDao = issueRepository.findByTypeContainingIgnoreCaseAndMessageContainingIgnoreCase(event.getType(),event.getMessage());
        if(issueDao.isEmpty())
        {
            IssueDao newIssueDao = event.toIssueDao(projectDao.get().getId());
            newIssueDao = issueRepository.saveAndFlush(newIssueDao);
            event.setIssueId(newIssueDao.getId());
        }
        else {
            event.setIssueId(issueDao.get().getId());
        }
        eventRepository.saveAndFlush(event.toEventDao());
        return "Resource created.";
    }
}
