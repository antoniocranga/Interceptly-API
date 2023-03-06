package com.example.authmicroservice.repository;

import com.example.authmicroservice.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDao,Integer> {
    Optional<UserDao> findByEmail(String email);
}
