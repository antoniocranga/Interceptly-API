package com.interceptly.api.dto.post;

import com.interceptly.api.util.enums.PermissionEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionPostDto {
    @NotNull
    private Integer projectId;
    @NotNull
    private PermissionEnum permission;

    @NotNull
    private String email;
}
