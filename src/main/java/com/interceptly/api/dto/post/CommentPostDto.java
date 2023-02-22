package com.interceptly.api.dto.post;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentPostDto {
    @NotNull
    private Integer projectId;

    @NotNull
    private Integer issueId;

    @NotBlank
    private String comment;
}
