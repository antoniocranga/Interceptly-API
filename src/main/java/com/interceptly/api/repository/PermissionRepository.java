package com.interceptly.api.repository;

import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.dao.utils.PermissionsOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionDao, Integer> {
    List<PermissionDao> findAllByUserId(Integer userId);

    Optional<PermissionDao> findByUserIdAndProjectId(Integer userId, Integer projectId);

    @Query(value = "select p.user_id as id, (select u.email from users u where u.id = p.user_id) as email, p.permission as permission, p.created_at as createdAt, p.updated_at as updatedAt, (select u.email from users u where u.id = p.created_by) as createdBy from permissions p where p.project_id = ?1" ,nativeQuery = true)
    List<PermissionsOnly> findAllByProjectIdCustom(Integer projectId);

    List<PermissionDao> findAllByProjectId(Integer projectId);
}
