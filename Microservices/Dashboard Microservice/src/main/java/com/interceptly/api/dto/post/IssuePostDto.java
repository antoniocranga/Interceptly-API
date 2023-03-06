package com.interceptly.api.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IssuePostDto {
    @NotNull
    private String email;

    @NotNull
    private Integer projectId;

    @NotNull
    private Integer issueId;
}
