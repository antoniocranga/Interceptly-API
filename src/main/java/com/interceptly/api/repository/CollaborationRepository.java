package com.interceptly.api.repository;

import com.interceptly.api.dao.CollaborationDao;
import com.interceptly.api.util.enums.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CollaborationRepository extends JpaRepository<CollaborationDao, Integer> {

    Optional<CollaborationDao> findByUserIdAndIssueId(Integer userId, Integer issueId);
}
