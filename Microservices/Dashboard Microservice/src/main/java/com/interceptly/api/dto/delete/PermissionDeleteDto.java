package com.interceptly.api.dto.delete;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PermissionDeleteDto {
    @NotNull
    private Integer userId;

    @NotNull
    private Integer projectId;
}
