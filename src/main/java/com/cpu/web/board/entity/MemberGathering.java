package com.cpu.web.board.entity;

import com.cpu.web.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class MemberGathering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_gathering_id")
    private Long memberGatheringId;

    @CreationTimestamp
    private Timestamp joinDate;

    @Column(name = "is_leader", nullable = false)
    private Boolean isLeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id", nullable = false)
    @JsonIgnore
    private Gathering gathering;
}
