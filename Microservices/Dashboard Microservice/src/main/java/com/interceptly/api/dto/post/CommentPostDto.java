package com.interceptly.api.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentPostDto {
    @NotNull
    private Integer projectId;

    @NotNull
    private Integer issueId;

    @NotBlank
    private String comment;
}
