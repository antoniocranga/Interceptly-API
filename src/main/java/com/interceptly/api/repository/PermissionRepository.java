package com.interceptly.api.repository;

import com.interceptly.api.dao.PermissionDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionDao, Integer> {
    List<PermissionDao> findAllByUserId(Integer userId);

    Optional<PermissionDao> findByUserIdAndProjectId(Integer userId, Integer projectId);
}
