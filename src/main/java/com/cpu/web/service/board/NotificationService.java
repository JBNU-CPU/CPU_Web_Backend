package com.cpu.web.service.board;

import com.cpu.web.dto.board.NotificationDTO;
import com.cpu.web.entity.board.Notification;
import com.cpu.web.repository.board.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 페이징된 전체 글 조회
    public Page<NotificationDTO> getAllNotifications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findAll(pageable).map(this::convertToDTO);
    }

    // 특정 글 조회
    public Optional<NotificationDTO> getNotificationById(Long id) {
        return notificationRepository.findById(id).map(this::convertToDTO);
    }

    // 글 저장
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationDTO.toNotificationEntity();
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    // 글 수정
    public NotificationDTO updateNotification(Long id, NotificationDTO notificationDTO) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notification ID: " + id));
        notification.setTitle(notificationDTO.getTitle());
        notification.setContent(notificationDTO.getContent());
        Notification updatedNotification = notificationRepository.save(notification);
        return convertToDTO(updatedNotification);
    }

    // 글 삭제
    public void deleteNotification(Long id) {
        if(!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid notification ID: " + id);
        }
        notificationRepository.deleteById(id);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(notification);
    }
}
