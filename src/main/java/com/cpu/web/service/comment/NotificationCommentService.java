package com.cpu.web.service.comment;

import com.cpu.web.dto.comment.NotificationCommentDTO;
import com.cpu.web.entity.comment.NotificationComment;
import com.cpu.web.entity.board.Notification;
import com.cpu.web.repository.comment.NotificationCommentRepository;
import com.cpu.web.repository.board.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationCommentService {

    private NotificationCommentRepository notificationCommentRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationCommentService(NotificationCommentRepository notificationCommentRepository, NotificationRepository notificationRepository) {
        this.notificationCommentRepository = notificationCommentRepository;
        this.notificationRepository = notificationRepository;
    }

    // 특정 공지사항에 대한 모든 댓글 조회
    public List<NotificationCommentDTO> getAllCommentsForNotification(int notificationId) {
        return notificationCommentRepository.findByNotificationId(notificationId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 ID의 댓글 조회
    public NotificationCommentDTO getCommentById(int id) {
        Optional<NotificationComment> comment = notificationCommentRepository.findById(id);
        return comment.map(this::convertToDTO).orElse(null);
    }

    // 새로운 댓글 생성
    public NotificationCommentDTO createComment(NotificationCommentDTO commentDTO) {
        Optional<Notification> notification = notificationRepository.findById(commentDTO.getNotificationId());
        if (notification.isPresent()) {
            NotificationComment comment = convertToEntity(commentDTO);
            comment.setPostDate(LocalDateTime.now());
            comment.setNotification(notification.get());
            NotificationComment savedComment = notificationCommentRepository.save(comment);
            return convertToDTO(savedComment);
        } else {
            throw new RuntimeException("Notification not found");
        }
    }

    // 댓글 업데이트
    public NotificationCommentDTO updateComment(int id, NotificationCommentDTO commentDTO) {
        Optional<NotificationComment> existingComment = notificationCommentRepository.findById(id);
        if (existingComment.isPresent()) {
            NotificationComment comment = existingComment.get();
            comment.setContents(commentDTO.getContents());
            NotificationComment updatedComment = notificationCommentRepository.save(comment);
            return convertToDTO(updatedComment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    // 댓글 삭제
    public void deleteComment(int id) {
        notificationCommentRepository.deleteById(id);
    }

    // NotificationComment 엔티티를 NotificationCommentDTO로 변환
    private NotificationCommentDTO convertToDTO(NotificationComment comment) {
        return NotificationCommentDTO.builder()
                .id(comment.getId())
                .studentNumber(comment.getStudentNumber())
                .contents(comment.getContents())
                .postDate(comment.getPostDate())
                .notificationId(comment.getNotification().getId())
                .build();
    }

    // NotificationCommentDTO를 NotificationComment 엔티티로 변환
    private NotificationComment convertToEntity(NotificationCommentDTO commentDTO) {
        return NotificationComment.builder()
                .id(commentDTO.getId())
                .studentNumber(commentDTO.getStudentNumber())
                .contents(commentDTO.getContents())
                .postDate(commentDTO.getPostDate())
                .build();
    }
}

