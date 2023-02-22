package com.interceptly.api.controller;

import com.interceptly.api.dao.*;
import com.interceptly.api.dao.composites.UserIssueComposite;
import com.interceptly.api.dao.composites.UserProjectComposite;
import com.interceptly.api.dao.utils.TagsOnly;
import com.interceptly.api.dto.IssueDto;
import com.interceptly.api.dto.NotificationDto;
import com.interceptly.api.dto.get.OverviewGetDto;
import com.interceptly.api.dto.get.StatisticsGetDto;
import com.interceptly.api.dto.patch.IssuesPatchDto;
import com.interceptly.api.dto.patch.ProjectPatchDto;
import com.interceptly.api.dto.post.IssuePostDto;
import com.interceptly.api.dto.post.PermissionPostDto;
import com.interceptly.api.dto.post.ProjectPostDto;
import com.interceptly.api.repository.*;
import com.interceptly.api.util.NotificationUtil;
import com.interceptly.api.util.PermissionUtil;
import com.interceptly.api.util.enums.IssueSortEnum;
import com.interceptly.api.util.enums.IssueStatusEnum;
import com.interceptly.api.util.enums.NotificationTypeEnum;
import com.interceptly.api.util.enums.PermissionEnum;
import com.interceptly.api.util.enums.converters.DirectionEnumConverter;
import com.interceptly.api.util.enums.converters.IssueSortEnumConverter;
import com.interceptly.api.util.enums.converters.IssueStatusEnumConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@RequestMapping("/projects")
@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class ProjectController {
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PermissionUtil permissionUtil;
    @Autowired
    NotificationUtil notificationUtil;
    @Autowired
    CollaborationRepository collaborationRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<PermissionDao> getProjects(@NotNull JwtAuthenticationToken authenticationToken) {
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        return permissionRepository.findAllByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PermissionDao postProject(@NotNull JwtAuthenticationToken authenticationToken, @NotNull @Valid @RequestBody ProjectPostDto projectDto) {
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        projectDto.setOwner(userId);
        ProjectDao projectDao = projectRepository.saveAndFlush(projectDto.toProjectDao());
        PermissionDao permissionDao = PermissionDao.builder().id(new UserProjectComposite(userId, projectDao.getId())).project(projectDao).permission(PermissionEnum.OWNER).build();
        log.debug(permissionDao.toString());
        return permissionRepository.saveAndFlush(permissionDao);
    }

    @GetMapping("/{projectId}")
    public PermissionDao getProject(@NotNull JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId) {
        return permissionUtil.getPermission(authenticationToken, projectId);
    }


    @PatchMapping("/{projectId}")
    public PermissionDao patchProject(@NotNull JwtAuthenticationToken authenticationToken, @NotNull @Valid @RequestBody ProjectPatchDto projectDto, @PathVariable Integer projectId) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, projectId);
        permissionUtil.checkPermission(permissionDao, PermissionEnum.OWNER);
        ProjectDao projectDao = permissionDao.getProject();
        List<Field> fields = new LinkedList<>();
        fields.addAll(List.of(projectDto.getClass().getDeclaredFields()));
        fields.addAll(List.of(projectDto.getClass().getSuperclass().getDeclaredFields()));
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Field referenceField = ReflectionUtils.findField(ProjectDao.class, f.getName());
                if (referenceField != null) {
                    referenceField.setAccessible(true);
                    ReflectionUtils.setField(referenceField, projectDao, f.get(projectDto));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        projectDao.setOwner(permissionDao.getUserId());
        permissionDao.setProject(projectDao);
        return permissionRepository.saveAndFlush(permissionDao);
    }

    @GetMapping("/{projectId}/overview")
    public List<OverviewGetDto> getOverview(
            @NotNull JwtAuthenticationToken authenticationToken,
            @PathVariable Integer projectId
    ) {
        LocalDateTime localDateTime = LocalDateTime.now();
        permissionUtil.getPermission(authenticationToken, projectId);

        LocalDateTime start = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonthValue(), 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);


        LocalDateTime lastMonthStart = start.minusMonths(1);

        Long events = eventRepository.countByProjectIdAndCreatedAtBetweenStartAndEnd(projectId, start, end);
        Long solvedIssues = issueRepository.countByProjectIdAndStatusAndUpdatedAtBetweenStartAndEnd(projectId, IssueStatusEnum.RESOLVED, start, end);
        Long remainingIssues = issueRepository.countByProjectIdAndStatus(projectId, IssueStatusEnum.ACTIVE);

        Long lastMonthEvents = eventRepository.countByProjectIdAndCreatedAtBetweenStartAndEnd(projectId, lastMonthStart, start);
        Long lastMonthSolvedIssues = issueRepository.countByProjectIdAndStatusAndUpdatedAtBetweenStartAndEnd(projectId, IssueStatusEnum.RESOLVED, lastMonthStart, start);

        Long allSolvedIssues = issueRepository.countByProjectIdAndStatus(projectId, IssueStatusEnum.RESOLVED);
        Long allEvents = eventRepository.countByProjectId(projectId);
        Long allRemainingIssues = issueRepository.countByProjectIdAndStatusNot(projectId, IssueStatusEnum.RESOLVED);

        OverviewGetDto eventsItem = new OverviewGetDto("Events", allEvents, events, lastMonthEvents, "The number of all events recorded.");
        OverviewGetDto issuesItem = new OverviewGetDto("Issues solved", allSolvedIssues, solvedIssues, lastMonthSolvedIssues, "The number of all issues marked as [RESOLVED].");
        OverviewGetDto remainingIssuesItem = new OverviewGetDto("Issues remaining", allRemainingIssues, remainingIssues, null, "The number of all issues that are [BLOCKED, IGNORED, ACTIVE]");

        return List.of(eventsItem, issuesItem, remainingIssuesItem);
    }

    @GetMapping("/{projectId}/issues")
    public Page<IssueDao> getIssues(@NotNull JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId, @RequestParam(defaultValue = "25") Integer size, @RequestParam(defaultValue = "0") Integer page, @RequestParam(value = "status", defaultValue = "ACTIVE") IssueStatusEnum status, @RequestParam(value = "range", defaultValue = "14") Integer range, @RequestParam(value = "sortBy", defaultValue = "lastSeen") IssueSortEnum sortBy, @RequestParam(value = "direction", defaultValue = "desc") Sort.Direction direction, @RequestParam(value = "type", defaultValue = "") String type, @RequestParam(value = "title", defaultValue = "") String title) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, projectId);
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime newDateTime = localDateTime.minusDays(range);
        Pageable paging = PageRequest.of(page, size, Sort.by(direction, sortBy.getValue()));
        if (range == 0) {
            return issueRepository.findAllByStatusAndProjectIdAndTitleContainsIgnoreCaseAndTypeContainsIgnoreCase(status, projectId, title, type, paging);
        } else {
            return issueRepository.findAllByStatusAndLastSeenAfterAndProjectIdAndTitleContainsIgnoreCaseAndTypeContainsIgnoreCase(status, newDateTime, projectId, title, type, paging);
        }
    }

    @GetMapping("/{projectId}/issues/{issueId}")
    public Optional<IssueDao> getIssue(@NotNull JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId, @PathVariable Integer issueId, @RequestParam(defaultValue = "0") Integer page) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, projectId);
        Pageable paging = PageRequest.of(page, 1, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<EventDao> events = eventRepository.findAllByIssueId(issueId, paging);
        Optional<IssueDao> issueDao = issueRepository.findById(issueId);
        if (issueDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        issueDao.ifPresent(issue -> issue.setEvents(events));
        return issueDao;
    }

    @PatchMapping("/{projectId}/issues")
    @Transactional
    public String patchIssues(@NotNull JwtAuthenticationToken authenticationToken, @RequestBody IssuesPatchDto issuesPatchDto, @PathVariable Integer projectId) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, projectId);
        issueRepository.updateIssuesBulk(issuesPatchDto.getStatus(), issuesPatchDto.getIds());
        return "Status updated";
    }

    @PatchMapping("/{projectId}/issues/{issueId}")
    public IssueDao updateIssue(@NotNull JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId, @PathVariable Integer issueId, @RequestBody IssueDto updatedIssue) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, projectId);
        if (permissionDao.getPermission() == PermissionEnum.VIEW) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        Optional<IssueDao> issueDao = issueRepository.findById(issueId);
        if (issueDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        issueRepository.saveAndFlush(updatedIssue.toDao(issueDao.get()));
        return issueDao.get();
    }

    @PatchMapping("/{projectId}/reset-key")
    public PermissionDao resetKey(@NotNull JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, projectId);
        permissionUtil.checkPermission(permissionDao, PermissionEnum.OWNER);
        permissionDao.getProject().setApiKey(UUID.randomUUID());
        permissionRepository.saveAndFlush(permissionDao);
        return permissionDao;
    }


    @GetMapping("/{projectId}/statistics")
    public StatisticsGetDto getStatistics(@NotNull JwtAuthenticationToken authenticationToken, @PathVariable Integer projectId, @RequestParam(name = "range", defaultValue = "14") Integer range) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, projectId);
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime start = localDateTime.minusDays(range);
        List<String> browsers = eventRepository.selectBrowsers(projectId);
        List<String> deviceTypes = eventRepository.selectDeviceTypes(projectId);
        List<Map<String, Object>> eventsByBrowser = formatTags(eventRepository.countByBrowserAndFormattedDate(projectId, start, localDateTime), range);
        List<Map<String, Object>> eventsByDeviceType = formatTags(eventRepository.countByDeviceTypeAndFormattedDate(projectId, start, localDateTime), range);
        List<Map<String, Object>> issues = formatTags(issueRepository.countByIssuesAndFormattedDate(projectId, start, localDateTime), range);
        List<Map<String, Object>> events = formatTags(eventRepository.countByEventsAndFormattedDate(projectId, start, localDateTime), range);
        List<Map<String, Object>> solvedIssues = formatTags(issueRepository.countByIssuesAndStatusAndFormattedDate(projectId, IssueStatusEnum.RESOLVED, start, localDateTime), range);
        List<OverviewGetDto> overviewGetDto = getOverview(authenticationToken, projectId);
        return new StatisticsGetDto(issues, events, solvedIssues, eventsByBrowser, eventsByDeviceType, browsers, deviceTypes, overviewGetDto);
    }

    private List<Map<String, Object>> formatTags(List<TagsOnly> tags, Integer range) {
        LocalDate localDate = LocalDate.now();
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Long>> valuesForTags = new ArrayList<>();
        Map<String, Integer> indexMap = new HashMap<>();
        for (TagsOnly tagsOnly : tags) {
            Integer index = indexMap.get(tagsOnly.getTag());
            if (index == null) {
                Map<String, Long> valueForTags = new TreeMap<>();
                for (int i = range; i > 0; i--) {
                    valueForTags.put(localDate.minusDays(i).toString(), 0L);
                }
                valuesForTags.add(valueForTags);
                index = valuesForTags.size() - 1;
                indexMap.put(tagsOnly.getTag(), index);
            }
            valuesForTags.get(index).put(tagsOnly.getDate().toString(), tagsOnly.getValue());
        }
        for (Map.Entry<String, Integer> entry : indexMap.entrySet()) {
            String tag = entry.getKey();
            Integer index = entry.getValue();
            Map<String, Long> valueForTags = valuesForTags.get(index);
            List<Map<String, Object>> data = new ArrayList<>();
            for (Map.Entry<String, Long> dateAndValue : valueForTags.entrySet()) {
                Map<String, Object> coordinates = new HashMap<>();
                String date = dateAndValue.getKey();
                Long value = dateAndValue.getValue();
                coordinates.put("x", date);
                coordinates.put("y", value);
                data.add(coordinates);
            }
            Map<String, Object> series = new HashMap<>();
            series.put("name", tag);
            series.put("data", data);
            list.add(series);
        }
        return list;
    }

    @PostMapping("/permissions")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String assignPermission(@NotNull JwtAuthenticationToken authenticationToken, @RequestBody @Valid PermissionPostDto permission) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, permission.getProjectId());
        Optional<UserDao> requestOwner = userRepository.findById(permissionDao.getUserId());
        Optional<UserDao> user = userRepository.findByEmail(permission.getEmail());
        if (user.isEmpty() || requestOwner.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        log.info(permission.toString());
        if (permissionDao.getPermission() == PermissionEnum.ADMIN || permissionDao.getPermission() == PermissionEnum.OWNER || Objects.equals(permissionDao.getUserId(), user.get().getId())) {
            Optional<PermissionDao> checkedPermission = permissionRepository.findByUserIdAndProjectId(user.get().getId(), permission.getProjectId());
            if (checkedPermission.isPresent()) {
                log.info(checkedPermission.get().toString());
                checkedPermission.get().setPermission(permission.getPermission());
                permissionRepository.saveAndFlush(checkedPermission.get());
            } else {
                PermissionDao newPermissionDao = PermissionDao.builder().id(new UserProjectComposite(user.get().getId(), permission.getProjectId())).project(permissionDao.getProject()).permission(permission.getPermission()).build();
                permissionRepository.saveAndFlush(newPermissionDao);
            }
            NotificationDto notificationDto = NotificationDto.builder().type(NotificationTypeEnum.PROJECT_PERMISSION).projectId(permissionDao.getProjectId()).sentBy(permissionDao.getUserId()).emailBy(requestOwner.get().getEmail()).sentTo(user.get().getId()).emailTo(user.get().getEmail()).build();
            notificationUtil.sendNotificationToSpecific(notificationDto);
            return "Permission created";
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
    }

    @PostMapping("/issues/collaborations")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String postCollaboration(@NotNull JwtAuthenticationToken authenticationToken, @RequestBody @Valid IssuePostDto issuePostDto) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, issuePostDto.getProjectId());
        permissionUtil.checkPermission(permissionDao, PermissionEnum.ADMIN);
        Optional<UserDao> user = userRepository.findByEmail(issuePostDto.getEmail());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, null);
        }
        Optional<CollaborationDao> collaborationDao = collaborationRepository.findByUserIdAndIssueId(user.get().getId(), issuePostDto.getIssueId());
        if (collaborationDao.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, null);
        } else {
            CollaborationDao newCollaborationDao = CollaborationDao.builder().id(new UserIssueComposite(user.get().getId(), issuePostDto.getIssueId())).userId(user.get().getId()).issueId(issuePostDto.getIssueId()).createdBy(permissionDao.getUserId()).build();
            collaborationRepository.saveAndFlush(newCollaborationDao);
            NotificationDto notificationDto = NotificationDto.builder().type(NotificationTypeEnum.ISSUE_COLLABORATION).sentBy(permissionDao.getUserId()).emailBy(permissionDao.getEmail()).sentTo(user.get().getId()).emailTo(user.get().getEmail()).issueId(issuePostDto.getIssueId()).projectId(issuePostDto.getProjectId()).build();
            notificationUtil.sendNotificationToSpecific(notificationDto);
        }
        return "Collaboration created.";
    }

    @DeleteMapping("/issues/collaborations")
    public String deleteCollaboration(@NotNull JwtAuthenticationToken authenticationToken, @RequestBody @Valid IssuePostDto issuePostDto) {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, issuePostDto.getProjectId());
        permissionUtil.checkPermission(permissionDao, PermissionEnum.ADMIN);
        Optional<UserDao> user = userRepository.findByEmail(issuePostDto.getEmail());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, null);
        }
        Optional<CollaborationDao> collaborationDao = collaborationRepository.findByUserIdAndIssueId(user.get().getId(), issuePostDto.getIssueId());
        if (collaborationDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null);
        }
        collaborationRepository.delete(collaborationDao.get());
        return "Collaboration deleted.";
    }

    @InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(IssueSortEnum.class, new IssueSortEnumConverter());
        webDataBinder.registerCustomEditor(Sort.Direction.class, new DirectionEnumConverter());
        webDataBinder.registerCustomEditor(IssueStatusEnum.class, new IssueStatusEnumConverter());
    }
}
