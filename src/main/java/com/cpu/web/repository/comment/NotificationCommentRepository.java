package com.cpu.web.repository.comment;

import com.cpu.web.entity.comment.NotificationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationCommentRepository extends JpaRepository<NotificationComment, Integer> {
    // 특정 공지사항에 대한 모든 댓글 조회를 위한 메서드
    List<NotificationComment> findByNotificationId(int notificationId);
}
