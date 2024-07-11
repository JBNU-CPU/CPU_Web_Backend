package com.cpu.web.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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

}
