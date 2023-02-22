package com.interceptly.api.dto.patch;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentPatchDto {
    @NotNull
    private Integer id;
    @NotNull
    private Integer projectId;
    @NotNull
    private String comment;
}
