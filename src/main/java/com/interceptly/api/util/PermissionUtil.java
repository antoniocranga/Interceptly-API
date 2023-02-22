package com.interceptly.api.util;

import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.repository.PermissionRepository;
import com.interceptly.api.util.enums.PermissionEnum;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class PermissionUtil {

    @Autowired
    PermissionRepository permissionRepository;

    public PermissionDao getPermission(@NotNull JwtAuthenticationToken authenticationToken, @NotNull Integer projectId){
        Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
        Optional<PermissionDao> permissionDao = permissionRepository.findByUserIdAndProjectId(userId, projectId);
        if (permissionDao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
        return permissionDao.get();
    }

    public void checkPermission(@NotNull PermissionDao permissionDao, @NotNull PermissionEnum desiredPermission) {
        if (permissionDao.getPermission().ordinal() < desiredPermission.ordinal()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, null);
        }
    }
}
