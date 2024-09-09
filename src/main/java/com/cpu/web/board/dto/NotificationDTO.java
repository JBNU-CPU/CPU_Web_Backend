package com.cpu.web.board.dto;

import com.cpu.web.board.entity.Notification;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {
    private String title;
    private String content;

    public NotificationDTO(){}

    // entity => dto
    public NotificationDTO(Notification notification) {
        this.title = notification.getTitle();
        this.content = notification.getContent();
    }

    // dto => entity
    public Notification toNotificationEntity() {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setContent(content);
        return notification;
    }
}