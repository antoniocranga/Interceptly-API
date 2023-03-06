package com.example.eventsmicroservice.repository;


import com.example.eventsmicroservice.dao.ProjectDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectDao, Integer> {
    Optional<ProjectDao> findByApiKey(UUID apiKey);
}
