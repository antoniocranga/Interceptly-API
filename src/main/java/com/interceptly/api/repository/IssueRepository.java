package com.interceptly.api.repository;

import com.interceptly.api.dao.IssueDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<IssueDao,Integer> {
}
