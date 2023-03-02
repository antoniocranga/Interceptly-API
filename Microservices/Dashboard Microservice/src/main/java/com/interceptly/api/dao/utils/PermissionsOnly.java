package com.interceptly.api.dao.utils;

import com.interceptly.api.util.enums.PermissionEnum;

import java.time.LocalDateTime;

public interface PermissionsOnly {
    Integer getId();
    String getEmail();
    PermissionEnum getPermission();

    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    String getCreatedBy();
}
