package com.cpu.web.dto.board;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class NotificationDTO {
    private int id;
    private int studentNumber;
    private String title;
    private String contents;
    private LocalDateTime postDate;
    private boolean isAnonymous;
}
