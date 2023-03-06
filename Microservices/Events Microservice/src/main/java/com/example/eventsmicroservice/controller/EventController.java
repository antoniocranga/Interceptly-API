package com.example.eventsmicroservice.controller;

import com.example.eventsmicroservice.dao.EventDao;
import com.example.eventsmicroservice.dao.IssueDao;
import com.example.eventsmicroservice.dao.ProjectDao;
import com.example.eventsmicroservice.dto.EventDto;
import com.example.eventsmicroservice.repository.EventRepository;
import com.example.eventsmicroservice.repository.IssueRepository;
import com.example.eventsmicroservice.repository.ProjectRepository;
import com.example.eventsmicroservice.utils.enums.IssueStatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class EventController {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    ProjectRepository projectRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public String postEvent(HttpServletRequest request, @RequestBody @Valid EventDto event, @PathParam(value = "apiKey") @Size(min = 36, max = 36) String apiKey) throws JsonProcessingException {
        Optional<ProjectDao> projectDao = projectRepository.findByApiKey(UUID.fromString(apiKey));
        if (projectDao.isEmpty()) {
            log.error(apiKey);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        event.setRequest(request);
        Optional<IssueDao> issueDao = issueRepository.findByProjectIdAndTypeContainingIgnoreCaseAndMessageContainingIgnoreCase(projectDao.get().getId(), event.getType(), event.getMessage());
        if (issueDao.isEmpty()) {
            IssueDao newIssueDao = issueRepository.saveAndFlush(event.toIssueDao(projectDao.get().getId()));
            event.setIssueId(newIssueDao.getId());
        } else {
            if (issueDao.get().getStatus() == IssueStatusEnum.RESOLVED) {
                issueDao.get().setStatus(IssueStatusEnum.ACTIVE);
            }
            issueRepository.saveAndFlush(issueDao.get());
            event.setIssueId(issueDao.get().getId());
        }

        EventDao newEvent = event.toEventDao();
        newEvent.setProjectId(projectDao.get().getId());
        eventRepository.saveAndFlush(newEvent);
        return "Event created.";
    }

    @GetMapping
    public String getMapping(){
        return "Salut";
    }
}
