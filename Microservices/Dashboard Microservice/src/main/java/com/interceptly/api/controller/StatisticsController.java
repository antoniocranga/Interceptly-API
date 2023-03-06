package com.interceptly.api.controller;

import com.interceptly.api.dto.get.StatsGetDto;
import com.interceptly.api.repository.EventRepository;
import com.interceptly.api.repository.IssueRepository;
import com.interceptly.api.repository.ProjectRepository;
import com.interceptly.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
