package com.interceptly.api.dto.base;

import com.interceptly.api.dao.EventDao;
import com.interceptly.api.dao.IssueDao;
import com.interceptly.api.util.RequestUtil;
import com.interceptly.api.util.enums.IssueStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.JSONObject;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;


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
    private Map<String, Object> body;
    private String environment;
    private String packageName;
    private String packageVersion;
    private String version;
    private Integer issueId;
    private HttpServletRequest request;

    public EventDao toEventDao() {
        String contentType = request.getHeader("Content-Type");
        String userAgent = request.getHeader("User-Agent");
        String headers = contentType + ":" + userAgent;
        Map<String, Object> tags = new HashMap<>();
        try {
            tags.putAll(RequestUtil.parseUserAgent(userAgent));
        } catch (Exception ignored) {
        }
        if(body == null){
            body = new HashMap<>();
        }
        return EventDao.builder().
                type(type).
                message(message).
                issueId(issueId).
                headers(headers).
                userAgent(userAgent).
                name(name).
                stackTrace(stackTrace).
                body(new JSONObject(body).toString()).
                environment(environment).
                packageName(packageName).
                packageVersion(packageVersion).
                version(version).
                tags(tags.toString()).
                deviceType((String) tags.get("deviceType")).
                browserMajorVersion((String) tags.get("browserMajorVersion")).
                browser((String) tags.get("browser")).
                platformVersion((String) tags.get("platformVersion")).
                browserType((String) tags.get("browserType")).
                build();
    }

    public IssueDao toIssueDao(Integer projectId) {
        return IssueDao.builder().
                type(type).
                message(message).
                title(message).
                description(stackTrace != null ? stackTrace : message).
                projectId(projectId).
                status(IssueStatusEnum.ACTIVE).
                isBookmarked(false).
                build();
    }
}
