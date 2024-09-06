package com.cpu.web.service.board;

import com.cpu.web.dto.board.NotificationDTO;
import com.cpu.web.entity.board.Notification;
import com.cpu.web.repository.board.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 전체 글 조회
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
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
