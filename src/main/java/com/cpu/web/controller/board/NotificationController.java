package com.cpu.web.controller.board;

import com.cpu.web.dto.board.NotificationDTO;
import com.cpu.web.entity.board.Notification;
import com.cpu.web.service.board.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

// 공지 게시판 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    // 글 작성
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        Notification notification = notificationService.createNotification(notificationDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/notification/{id}")
                .buildAndExpand(notification.getNotificationId())
                .toUri();
        return ResponseEntity.created(location).body(notificationDTO);
    }

    // 전체 글 조회
    @GetMapping
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    // 특정 글 조회

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        Optional<NotificationDTO> notificationDTO = notificationService.getNotificationById(id);
        return notificationDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 글 수정
    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> updateNotification(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO) {
        NotificationDTO updatedNotificationDTO = notificationService.updateNotification(id, notificationDTO);
        return ResponseEntity.ok(updatedNotificationDTO);
    }

    // 글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

}
