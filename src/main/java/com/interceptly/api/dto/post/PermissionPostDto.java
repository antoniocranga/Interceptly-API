package com.interceptly.api.dto.post;

import com.interceptly.api.util.enums.PermissionEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class PermissionPostDto {
    @NotNull
    private Integer projectId;
    @NotNull
    private PermissionEnum permission;

    @NotNull
    private String email;
}
