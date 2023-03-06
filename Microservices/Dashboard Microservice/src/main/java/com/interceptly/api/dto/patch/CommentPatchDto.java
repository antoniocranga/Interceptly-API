package com.interceptly.api.dto.patch;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentPatchDto {
    @NotNull
    private Integer id;
    @NotNull
    private Integer projectId;
    @NotNull
    private String comment;
}
