package com.interceptly.api.repository;

import com.interceptly.api.dao.NotificationDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationDao, Integer> {

    List<NotificationDao> findAllBySentToOrderByCreatedAtDesc(Integer sentTo);

    List<NotificationDao> findAllBySentTo(Integer sentTo);
}
