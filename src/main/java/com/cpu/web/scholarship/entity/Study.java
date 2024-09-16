package com.cpu.web.scholarship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
//주의! 엔티티에는 Setter를 사용하지 않는것이 원칙
//엔티티는 DB와 바로 연결되므로 데이터를 자유롭게 변경할수 있는 Setter를 사용하지 않는것이 좋다
//원래는 생성자를 통해서만 엔티티의 값을 저장하고 데이터 변경을 위해선 메소드를 추가로 작성하는것이 원칙
public class Study {

    @Id//기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)//데이터 저장시 자동1증가해서 저장 ,strategy는 고유옵션
    @Column(name = "study_id", nullable = false, unique = true)//테이블 열 세부설정
    private Long studyId;//Long 말고 int로 해도됨 Long이 좀 더 범위가 넓어서 그럼

    @Column(name = "memberId", nullable = false)
    private Long memberId;

    @Column(name = "studyName", length = 100, nullable = false)
    private String studyName;

    @Enumerated(EnumType.STRING)//열거형 DB 맵핑용
    @Column(name = "studyType", nullable = false)
    private StudyType studyType;

    @Column(name = "maxMembers", nullable = false)
    private int maxMembers;

    @Column(name = "studyDescription")
    private String studyDescription;

    @CreationTimestamp
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp updatedDate;

    // todo 다른 엔티티 참조관계:
    // @ManyToOne
    // private Member member;

    // Enum for study_type
    public enum StudyType {
        세션, 스터디, 프로젝트
    }
}