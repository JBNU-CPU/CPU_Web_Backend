package com.cpu.web.entity.comment;

import com.cpu.web.entity.board.Notification;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class NotificationComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_comment_id", nullable = false)
    private int id;

    @Column(name = "student_number", nullable = false)
    private int studentNumber;

    @Column(name = "contents", length = 500, nullable = false)
    private String contents;

    @Column(name = "comment_date", nullable = false)
    private LocalDateTime postDate;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

}
