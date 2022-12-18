package com.interceptly.api.repository;

import com.interceptly.api.dao.IssueDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssueRepository extends JpaRepository<IssueDao,Integer> {

    Optional<IssueDao> findByTypeContainingIgnoreCase(String type);
    Optional<IssueDao> findByTypeContainingIgnoreCaseAndMessageContainingIgnoreCase(String type, String message);
}
