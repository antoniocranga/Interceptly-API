package com.interceptly.api.controller;

import com.interceptly.api.dao.NotificationDao;
import com.interceptly.api.dto.NotificationDto;
import com.interceptly.api.repository.NotificationRepository;
import com.interceptly.api.repository.ProjectRepository;
import com.interceptly.api.repository.UserRepository;
import com.interceptly.api.util.PermissionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PermissionUtil permissionUtil;

   @PatchMapping("/seen")
    public String seenNotification(@RequestBody NotificationDto notificationDto){
       Optional<NotificationDao> notificationDao = notificationRepository.findById(notificationDto.getId());
       if(notificationDao.isEmpty()){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, null);
       }
       else {
           notificationDao.get().setSeen(true);
           notificationRepository.saveAndFlush(notificationDao.get());
       }
       return "Notification seen.";
   }

   @GetMapping
    public List<NotificationDao> getNotifications(@NotNull JwtAuthenticationToken authenticationToken){
       Integer userId = Integer.parseInt(authenticationToken.getTokenAttributes().get("user_id").toString());
       return notificationRepository.findAllBySentToAndSeenFalse(userId);
   }
}
