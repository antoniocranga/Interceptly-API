package com.interceptly.api.repository;

import com.interceptly.api.dao.EventDao;
import com.interceptly.api.dao.PermissionDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventDao, UUID> {
    List<EventDao> findAllByIssueId(Integer issueId);
}
