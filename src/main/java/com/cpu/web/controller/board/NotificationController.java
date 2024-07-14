package com.cpu.web.controller.board;

import com.cpu.web.dto.board.NotificationDTO;
import com.cpu.web.service.board.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 공지 게시판 컨트롤러
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 전체 글 조회
    @GetMapping
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    public NotificationDTO getNotificationById(@PathVariable int id) {
        return notificationService.getNotificationById(id);
    }

    // 글 저장
    @PostMapping
    public NotificationDTO createNotification(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.createNotification(notificationDTO);
    }

    // 글 수정
    @PutMapping("/{id}")
    public NotificationDTO updateNotification(@PathVariable int id, @RequestBody NotificationDTO notificationDTO) {
        return notificationService.updateNotification(id, notificationDTO);
    }

    // 글 삭제
    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable int id) {
        notificationService.deleteNotification(id);
    }

}
