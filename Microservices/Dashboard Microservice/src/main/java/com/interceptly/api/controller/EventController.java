package com.interceptly.api.controller;

import com.interceptly.api.dao.EventDao;
import com.interceptly.api.dao.IssueDao;
import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.dao.ProjectDao;
import com.interceptly.api.dto.EventDto;
import com.interceptly.api.repository.EventRepository;
import com.interceptly.api.repository.IssueRepository;
import com.interceptly.api.repository.ProjectRepository;
import com.interceptly.api.util.PermissionUtil;
import com.interceptly.api.util.enums.IssueStatusEnum;
import com.interceptly.api.util.exports.ExcelDataExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/events")
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
    public String postEvent(HttpServletRequest request, @RequestBody @Valid EventDto event, @PathParam(value = "apiKey") @Size(min = 36, max = 36) String apiKey) {
        Optional<ProjectDao> projectDao = projectRepository.findByApiKey(UUID.fromString(apiKey));
        if (projectDao.isEmpty()) {
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
}
