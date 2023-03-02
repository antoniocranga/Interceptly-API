package com.interceptly.api.dto.delete;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentDeleteDto {
    @NotNull
    private Integer projectId;

    @NotNull
    private Integer id;
}
