package com.cpu.web.scholarship.dto;

import com.cpu.web.scholarship.entity.Study;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyDTO {
    private Long memberId;
    private String studyName;
    private String studyType; // Enum이 아닌 String으로 변경
    private int maxMembers;
    private String studyDescription;

    // 기본 생성자
    public StudyDTO() {}

    // Study 객체를 받아 StudyDTO 객체를 초기화하는 생성자
    public StudyDTO(Study study) {
        this.memberId = study.getMemberId();
        this.studyName = study.getStudyName();
        this.studyType = study.getStudyType().name(); // Enum → String 변환
        this.maxMembers = study.getMaxMembers();
        this.studyDescription = study.getStudyDescription();
    }

    // DTO 정보를 사용하여 Study 엔티티 객체를 생성하는 메소드
    public Study toStudyEntity() {
        Study study = new Study();
        study.setMemberId(this.memberId);
        study.setStudyName(this.studyName);
        study.setStudyType(Study.StudyType.valueOf(this.studyType.toLowerCase())); // String → Enum 변환
        study.setMaxMembers(this.maxMembers);
        study.setStudyDescription(this.studyDescription);
        return study;
    }
}
