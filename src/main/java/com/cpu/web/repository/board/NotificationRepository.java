package com.cpu.web.repository.board;

import com.cpu.web.entity.board.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
