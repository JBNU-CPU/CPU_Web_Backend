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

    // 명시적인 getter와 setter 추가 (필요 시)
    public boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
}
