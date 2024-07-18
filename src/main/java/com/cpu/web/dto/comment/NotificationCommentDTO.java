package com.cpu.web.dto.comment;


import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class NotificationCommentDTO {

    private int id;
    private int studentNumber;
    private String contents;
    private LocalDateTime postDate;
    private int notificationId;

}
