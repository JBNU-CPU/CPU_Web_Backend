package com.cpu.web.entity.board;

import com.cpu.web.entity.comment.NotificationComment;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id", nullable = false)
    private int id;

    @Column(name = "student_number", nullable = false)
    private int studentNumber;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Column(name = "contents", length = 500, nullable = false)
    private String contents;

    @Column(name = "post_date", nullable = false)
    private LocalDateTime postDate;

    @Column(name = "is_anonymous", nullable = false)
    private boolean isAnonymous;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationComment> notificationComments;

    // 명시적인 getter와 setter 추가
    public boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
}
