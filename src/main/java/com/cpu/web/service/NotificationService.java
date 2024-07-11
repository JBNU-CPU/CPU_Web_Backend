package com.cpu.web.service;


import com.cpu.web.dto.NotificationDTO;
import com.cpu.web.entity.Notification;
import com.cpu.web.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(int id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        return notification.map(this::convertToDTO).orElse(null);
    }

    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = convertToEntity(notificationDTO);
        notification.setPostDate(LocalDateTime.now());
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    public NotificationDTO updateNotification(int id, NotificationDTO notificationDTO) {
        Optional<Notification> existingNotification = notificationRepository.findById(id);
        if (existingNotification.isPresent()) {
            Notification notification = existingNotification.get();
            notification.setTitle(notificationDTO.getTitle());
            notification.setContents(notificationDTO.getContents());
            notification.setAnonymous(notificationDTO.isAnonymous());
            // 필요한 경우 다른 필드도 업데이트합니다.

            Notification updatedNotification = notificationRepository.save(notification);
            return convertToDTO(updatedNotification);
        } else {
            return null; // 또는 예외를 던질 수 있습니다.
        }
    }

    public void deleteNotification(int id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .studentNumber(notification.getStudentNumber())
                .title(notification.getTitle())
                .contents(notification.getContents())
                .postDate(notification.getPostDate())
                .isAnonymous(notification.isAnonymous())
                .build();
    }

    private Notification convertToEntity(NotificationDTO notificationDTO) {
        return Notification.builder()
                .id(notificationDTO.getId())
                .studentNumber(notificationDTO.getStudentNumber())
                .title(notificationDTO.getTitle())
                .contents(notificationDTO.getContents())
                .postDate(notificationDTO.getPostDate())
                .isAnonymous(notificationDTO.isAnonymous())
                .build();
    }
}

