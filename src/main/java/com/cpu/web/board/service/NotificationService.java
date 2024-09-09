package com.cpu.web.board.service;

import com.cpu.web.board.dto.NotificationDTO;
import com.cpu.web.board.entity.Notification;
import com.cpu.web.board.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 글 저장
    public Notification createNotification(NotificationDTO notificationDTO) {

        String title = notificationDTO.getTitle();
        String content = notificationDTO.getContent();

        // 제목 유효한지
        if (title == null) {
            throw new IllegalArgumentException("제목이 유효하지 않습니다.");
        } else if (title.isEmpty()) {
            throw new IllegalArgumentException("제목이 유효하지 않습니다.");
        } else if (title.isBlank()) {
            throw new IllegalArgumentException("제목이 유효하지 않습니다.");
        }

        // 내용 유효한지
        if (content == null) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isEmpty()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isBlank()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        }

        Notification notification = notificationDTO.toNotificationEntity();
        return notificationRepository.save(notification);
    }

    // 페이징 처리된 공지 글 조회
    public Page<NotificationDTO> getAllNotifications(int page, int size) {
        Page<Notification> notifications = notificationRepository.findAll(PageRequest.of(page, size));
        return notifications.map(NotificationDTO::new);
    }

    // 특정 글 조회
    public Optional<NotificationDTO> getNotificationById(Long id) {
        return notificationRepository.findById(id).map(NotificationDTO::new);
    }

    // 글 수정
    public NotificationDTO updateNotification(Long id, NotificationDTO notificationDTO) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id));
        notification.setTitle(notificationDTO.getTitle());
        notification.setContent(notificationDTO.getContent());
        Notification updatedNotification = notificationRepository.save(notification);
        return new NotificationDTO(updatedNotification);
    }

    // 글 삭제
    public void deleteNotification(Long id) {
        if(!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id);
        }
        notificationRepository.deleteById(id);
    }

}