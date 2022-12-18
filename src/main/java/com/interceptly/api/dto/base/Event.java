package com.interceptly.api.dto.base;

import com.interceptly.api.dao.EventDao;
import com.interceptly.api.dao.IssueDao;
import com.interceptly.api.util.RequestUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;


@MappedSuperclass
@EqualsAndHashCode
@Data
public abstract class Event {
    @NotNull
    @NotBlank(message = "Error type not existent.")
    private String type;
    @NotNull
    @NotBlank(message = "Message not existent.")
    private String message;
    private String name;
    private String stackTrace;
    private String body;
    private String environment;
    private String packageName;
    private String packageVersion;
    private Integer issueId;
    private HttpServletRequest request;

    public EventDao toEventDao() {
        String contentType = request.getHeader("Content-Type");
        String userAgent = request.getHeader("User-Agent");
        String headers = contentType + ":" + userAgent;
        String clientOs = RequestUtil.clientOs(userAgent);
        String browser = RequestUtil.browser(userAgent);

        return EventDao.builder().
                type(type).
                message(message).
                issueId(issueId).
                headers(headers).
                browser(browser).
                clientOs(clientOs).
                name(name).
                stackTrace(stackTrace).
                body(body).
                environment(environment).
                packageName(packageName).
                packageVersion(packageVersion).
                build();
    }

    public IssueDao toIssueDao(Integer projectId) {
        return IssueDao.builder().
                title(message).
                description(stackTrace != null ? stackTrace : message).
                projectId(projectId).
                build();
    }
}
