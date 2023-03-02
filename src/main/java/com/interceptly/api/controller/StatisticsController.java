package com.interceptly.api.controller;

import com.interceptly.api.dao.EventDao;
import com.interceptly.api.dao.IssueDao;
import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.dao.ProjectDao;
import com.interceptly.api.dto.EventDto;
import com.interceptly.api.dto.get.StatsGetDto;
import com.interceptly.api.repository.EventRepository;
import com.interceptly.api.repository.IssueRepository;
import com.interceptly.api.repository.ProjectRepository;
import com.interceptly.api.repository.UserRepository;
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

@RequestMapping("/stats")
@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class StatisticsController {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public StatsGetDto getStats(){
        Long eventsCount = eventRepository.count();
        Long issuesCount = issueRepository.count();
        Long projectsCount = projectRepository.count();
        Long usersCount = userRepository.count();
        return new StatsGetDto(
                eventsCount,
                projectsCount,
                issuesCount,
                usersCount
        );
    }
}
