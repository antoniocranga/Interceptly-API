package com.interceptly.api.dto.delete;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDeleteDto {
    @NotNull
    private Integer projectId;

    @NotNull
    private Integer id;
}
