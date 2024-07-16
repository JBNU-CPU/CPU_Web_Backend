package com.cpu.web.controller.comment;

import com.cpu.web.dto.comment.NotificationCommentDTO;
import com.cpu.web.service.comment.NotificationCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications/{notificationId}/comments")
public class NotificationCommentController {

    private NotificationCommentService notificationCommentService;

    @Autowired
    public NotificationCommentController(NotificationCommentService notificationCommentService) {
        this.notificationCommentService = notificationCommentService;
    }

    // 특정 공지사항에 대한 모든 댓글 조회
    @GetMapping
    public List<NotificationCommentDTO> getAllComments(@PathVariable int notificationId) {
        return notificationCommentService.getAllCommentsForNotification(notificationId);
    }

    // 특정 ID의 댓글 조회
    @GetMapping("/{id}")
    public NotificationCommentDTO getCommentById(@PathVariable int id) {
        return notificationCommentService.getCommentById(id);
    }

    // 새로운 댓글 생성
    @PostMapping
    public NotificationCommentDTO createComment(@PathVariable int notificationId, @RequestBody NotificationCommentDTO commentDTO) {
        commentDTO.setNotificationId(notificationId);
        return notificationCommentService.createComment(commentDTO);
    }

    // 댓글 업데이트
    @PutMapping("/{id}")
    public NotificationCommentDTO updateComment(@PathVariable int id, @RequestBody NotificationCommentDTO commentDTO) {
        return notificationCommentService.updateComment(id, commentDTO);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable int id) {
        notificationCommentService.deleteComment(id);
    }
}
