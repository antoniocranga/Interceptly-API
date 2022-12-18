package com.interceptly.api.repository;

import com.interceptly.api.dao.ProjectDao;
import com.interceptly.api.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectDao,Integer> {
    Optional<ProjectDao> findByApiKey(UUID apiKey);
}
