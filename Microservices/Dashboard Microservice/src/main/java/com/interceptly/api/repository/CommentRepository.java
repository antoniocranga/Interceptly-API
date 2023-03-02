package com.interceptly.api.repository;

import com.interceptly.api.dao.CommentDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentDao, Integer> {
}
