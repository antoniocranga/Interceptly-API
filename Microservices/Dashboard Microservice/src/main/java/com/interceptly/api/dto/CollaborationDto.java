package com.interceptly.api.dto;

import lombok.Data;

@Data
public class CollaborationDto {
    private String email;
    private Integer projectId;
    private Integer issueId;

    private Integer userId;
}
