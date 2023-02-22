package com.interceptly.api.dto;

import com.interceptly.api.dao.NotificationDao;
import com.interceptly.api.util.enums.IssueStatusEnum;
import com.interceptly.api.util.enums.NotificationTypeEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class NotificationDto {
    private Integer id;
    private NotificationTypeEnum type;
    private Integer issueId;
    private IssueStatusEnum issueStatus;
    private Integer projectId;
    private Integer sentTo;
    private String emailTo;

    private Integer sentBy;
    private String emailBy;

    public NotificationDao toNotificationDao(){
        String message = null;
        String redirectUrl = null;
        switch (type){
            case PROJECT_PERMISSION -> {
                message = emailBy + " invited you to collaborate on the project " + projectId;
                redirectUrl = "http://localhost:3000/dashboard/projects/" + projectId;
            }
            case ISSUE_COLLABORATION -> {
                message = emailBy + " assigned you the issue " + issueId;
                redirectUrl = "http://localhost:3000/dashboard/projects/" + projectId + "/issues/" + issueId;
            }
            case ISSUE_UPDATE -> {
                message = emailBy + " updated the issue " + issueId + " to " + issueStatus.name();
                redirectUrl = "http://localhost:3000/dashboard/projects/" + projectId + "/issues/" + issueId;
            }
            case CRITICAL -> {
            }
        }
        return NotificationDao.builder().type(type).message(message).redirectUrl(redirectUrl).sentTo(sentTo).sentBy(sentBy).seen(false).build();
    }
}
