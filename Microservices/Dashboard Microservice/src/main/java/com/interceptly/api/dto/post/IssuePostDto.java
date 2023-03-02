package com.interceptly.api.dto.post;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IssuePostDto {
    @NotNull
    private String email;

    @NotNull
    private Integer projectId;

    @NotNull
    private Integer issueId;
}
