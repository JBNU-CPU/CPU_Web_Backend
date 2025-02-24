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
    @Column(name = "event_id", nullable = false, unique = true)
    private Long eventId;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "score", nullable = false, unique = true)
    private Integer score;
}
