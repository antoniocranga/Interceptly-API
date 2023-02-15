package com.interceptly.api.repository;

import com.interceptly.api.dao.PermissionDao;
import com.interceptly.api.util.enums.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionDao,Integer> {
    List<PermissionDao> findAllByUserId(Integer userId);
    Optional<PermissionDao> findByUserIdAndProjectId(Integer userId, Integer projectId);

    @Modifying
    @Query(value = "INSERT INTO permissions values (?1,?2,?3)",nativeQuery = true)
    void save(Integer userId, Integer projectId, PermissionEnum permission);
}
