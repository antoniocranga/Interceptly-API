package com.interceptly.api.util;

import com.interceptly.api.dao.NotificationDao;
import com.interceptly.api.dto.NotificationDto;
import com.interceptly.api.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationUtil {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    NotificationRepository notificationRepository;

    public void sendNotificationToSpecific(NotificationDto notificationDto) {
        NotificationDao notificationDao = notificationDto.toNotificationDao();
        notificationRepository.saveAndFlush(notificationDao);
        simpMessagingTemplate.convertAndSendToUser(notificationDto.getEmailTo(), "/specific", notificationDao);
    }

    public void sendNotificationToTopic(NotificationDto notificationDto) {
        NotificationDao notificationDao = notificationDto.toNotificationDao();
        notificationRepository.saveAndFlush(notificationDao);
        simpMessagingTemplate.convertAndSend("/topic/" + notificationDto.getProjectId(), notificationDao);
    }
}
