package com.cpu.web.scholarship.entity;

import com.cpu.web.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class MemberStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_study_id")
    private Long memberStudyId;

    @CreationTimestamp
    private Timestamp joinDate;

    @Column(name = "is_leader", nullable = false) // 팀장 여부
    private Boolean isLeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

}
