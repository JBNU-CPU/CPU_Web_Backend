package com.cpu.web.scholarship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long studyId;

    @Column(name = "member_id", nullable = false) // 개설자 ID
    private Long memberId;

    @Column(name = "study_name", nullable = false)
    private String studyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_type", nullable = false)
    private StudyType studyType;

    @Column(name = "max_members")
    private Integer maxMembers;

    @Column(name = "study_description", nullable = false)
    private String studyDescription;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp updateDate;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<Member_Study> joinedMemberList = new ArrayList<>();
    public enum StudyType {
        SESSION,
        STUDY,
        PROJECT
    }
}