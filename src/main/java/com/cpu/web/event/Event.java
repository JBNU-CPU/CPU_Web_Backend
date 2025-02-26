package com.cpu.web.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "nickname")
    private String nickname;
}
