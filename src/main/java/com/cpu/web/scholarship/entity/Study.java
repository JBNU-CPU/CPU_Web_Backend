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

    @Column(name = "is_accepted", nullable = false)
    private Boolean isAccepted = false;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "study_days", joinColumns = @JoinColumn(name = "study_id"))
    @Column(name = "day", nullable = false)
    private List<StudyDay> studyDays = new ArrayList<>();

    @Column(name = "study_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StudyType studyType;

    @Column(name = "max_members", nullable = false)
    private int maxMembers;

    @Column(name = "study_description", nullable = false)
    private String studyDescription;

    @Column(name = "tech_stack", nullable = false)
    private String techStack;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "etc")
    private String etc;

    @CreationTimestamp
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp updatedDate;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<MemberStudy> joinedMember = new ArrayList<>();

    // ENUM for study type
    public enum StudyType {
        session, study, project
    }

    // ENUM for study days
    public enum StudyDay {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }
}

