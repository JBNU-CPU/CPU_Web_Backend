package com.cpu.web.scholarship.dto;

import com.cpu.web.scholarship.entity.Study;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyDTO {
    private Long id; // ✅ 스터디 ID 추가
    private Long memberId;
    private Boolean isAccepted;
    private String studyName;
    private String studyType;
    private int maxMembers;
    private String studyDescription;
    private String techStack;
    private List<String> studyDays;
    private String location;
    private String etc;

    // 기본 생성자
    public StudyDTO() {}

    // Study 엔티티 → StudyDTO 변환
    public StudyDTO(Study study) {
        this.id = study.getStudyId();  // ✅ 스터디 ID 매핑
        this.memberId = study.getMemberId();
        this.isAccepted = study.getIsAccepted();
        this.studyName = study.getStudyName();
        this.studyType = study.getStudyType().name();
        this.maxMembers = study.getMaxMembers();
        this.studyDescription = study.getStudyDescription();
        this.techStack = study.getTechStack();
        this.studyDays = study.getStudyDays().stream().map(Enum::name).toList(); // ✅ ENUM → String 리스트 변환
        this.location = study.getLocation();
        this.etc = study.getEtc();
    }

    // StudyDTO → Study 변환
    public Study toStudyEntity() {
        Study study = new Study();
        study.setStudyId(this.id);  // ✅ ID 설정
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
