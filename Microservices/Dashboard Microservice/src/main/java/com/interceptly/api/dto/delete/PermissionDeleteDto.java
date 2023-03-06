package com.interceptly.api.dto.delete;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionDeleteDto {
    @NotNull
    private Integer userId;

    @NotNull
    private Integer projectId;
}
