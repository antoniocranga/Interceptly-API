package com.interceptly.api.controller;

import com.interceptly.api.dao.EventDao;
import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.repository.EventRepository;
import com.interceptly.api.util.PermissionUtil;
import com.interceptly.api.util.exports.ExcelDataExporter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/export")
@RestController
@Slf4j
public class ExportController {

    @Autowired
    PermissionUtil permissionUtil;

    @Autowired
    EventRepository eventRepository;

    @GetMapping("/events/excel")
    public void exportEvents(@NotNull JwtAuthenticationToken authenticationToken, HttpServletResponse response, @RequestParam Integer projectId, @RequestParam Integer issueId) throws IOException {
        PermissionDao permissionDao = permissionUtil.getPermission(authenticationToken, projectId);
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=interceptly_events_project_" + projectId + "_issue_" + issueId + "_" + currentDateTime + ".xlsx");
        List<EventDao> eventDaoList = eventRepository.findAllByIssueId(issueId);
        ExcelDataExporter exporter = new ExcelDataExporter(eventDaoList,"Events");
        exporter.export(response);
    }
}
