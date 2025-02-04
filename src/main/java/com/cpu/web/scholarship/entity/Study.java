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
    @Column(name = "study_id", nullable = false, unique = true)
    private Long studyId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "study_name", length = 100, nullable = false)
    private String studyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_type", nullable = false)
    private StudyType studyType;

    @Column(name = "max_members", nullable = false)
    private int maxMembers;

    @Column(name = "study_description", nullable = false)
    private String studyDescription;

    @Column(name = "tech_stack", nullable = false)  // 기술 스택 추가
    private String techStack;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_day", nullable = false)
    private StudyDay studyDay; // 진행 요일 추가

    @Column(name = "location", nullable = false)
    private String location; // 장소 추가

    @Column(name = "etc")
    private String etc; // 기타 추가

    @CreationTimestamp
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp updatedDate;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<MemberStudy> joinedMember = new ArrayList<>();

    // Enum for study_type
    public enum StudyType {
        session,
        study,
        project
    }

    // Enum for study_day
    public enum StudyDay {
        Mon, Tue, Wed, Thu, Fri, Sat, Sun
    }
}
