package com.interceptly.api.repository;

import com.interceptly.api.dao.CollaborationDao;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface CollaborationRepository extends JpaRepository<CollaborationDao, Integer> {

    Optional<CollaborationDao> findByUserIdAndIssueId(Integer userId, Integer issueId);

    @Transactional
    void deleteAllByUserId(Integer userId);
}
