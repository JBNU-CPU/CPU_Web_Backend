package com.cpu.web.scholarship.dto;

import com.cpu.web.scholarship.entity.Study;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyDTO {
    private Long memberId;
    private String studyName;
    private String studyType;
    private int maxMembers;
    private String studyDescription;
    private String techStack;
    private List<String> studyDays; // ✅ 복수 선택 가능하게 변경
    private String location;
    private String etc;

    // 기본 생성자
    public StudyDTO() {}

    // Study 객체를 받아 DTO 변환
    public StudyDTO(Study study) {
        this.memberId = study.getMemberId();
        this.studyName = study.getStudyName();
        this.studyType = study.getStudyType().name();
        this.maxMembers = study.getMaxMembers();
        this.studyDescription = study.getStudyDescription();
        this.techStack = study.getTechStack();
        this.studyDays = study.getStudyDays().stream().map(Enum::name).toList(); // ✅ ENUM → String 리스트 변환
        this.location = study.getLocation();
        this.etc = study.getEtc();
    }

    // DTO → Study 변환
    public Study toStudyEntity() {
        Study study = new Study();
        study.setMemberId(this.memberId);
        study.setStudyName(this.studyName);
        study.setStudyType(Study.StudyType.valueOf(this.studyType.toLowerCase()));
        study.setMaxMembers(this.maxMembers);
        study.setStudyDescription(this.studyDescription);
        study.setTechStack(this.techStack);
        study.setStudyDays(this.studyDays.stream().map(Study.StudyDay::valueOf).toList()); // ✅ String → ENUM 리스트 변환
        study.setLocation(this.location);
        study.setEtc(this.etc);
        return study;
    }
}
