package com.cpu.web.board.entity;

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
public class Gathering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_id", nullable = false, unique = true)
    private Long gatheringId;

    @Column(name = "gathering_title", length = 100, nullable = false)
    private String gatheringTitle;

    @Column(name = "gathering_content", length = 10000, nullable = false)
    private String gatheringContent;

    @Column(name = "max_members", nullable = false)
    private Integer maxMembers;

    @Column(name = "etc")
    private String etc;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "gathering_schedule", joinColumns = @JoinColumn(name = "gathering_id"))
    @Column(name = "gathering_entry")
    private List<String> gatheringDays = new ArrayList<>();

    @CreationTimestamp
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private Member leader;

}

