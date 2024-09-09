package com.cpu.web.board.repository;

import com.cpu.web.board.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 페이징 처리된 공지글 목록 조회
    Page<Notification> findAll(Pageable pageable);
}
