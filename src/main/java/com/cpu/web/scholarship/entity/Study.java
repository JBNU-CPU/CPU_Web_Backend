package com.cpu.web.scholarship.entity;

import com.cpu.web.member.entity.Member;
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

    @Column(name = "study_name", length = 100, nullable = false)
    private String studyName;

    @Column(name = "is_accepted", nullable = false)
    private Boolean isAccepted = false;

    @Column(name = "study_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StudyType studyType;

    @Column(name = "max_members", nullable = false)
    private int maxMembers;

    @Column(name = "study_description", length = 10000, nullable = false)
    private String studyDescription;

    @Column(name = "tech_stack", nullable = false)
    private String techStack;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "etc", length = 10000)
    private String etc;

    @ElementCollection
    @CollectionTable(name = "study_schedule", joinColumns = @JoinColumn(name = "study_id"))
    @Column(name = "schedule_entry")
    private List<String> studyDays = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private Member leader;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberStudy> joinedMember = new ArrayList<>();


    // ENUM for study type
    public enum StudyType {
        session, study, project
    }
}

