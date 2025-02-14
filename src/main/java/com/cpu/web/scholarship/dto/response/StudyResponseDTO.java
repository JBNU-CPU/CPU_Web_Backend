package com.cpu.web.scholarship.dto.response;

import com.cpu.web.scholarship.entity.Study;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class StudyResponseDTO {
    private Long studyId;
    private String studyName;
    private Study.StudyType studyType;
    private int maxMembers;
    private String studyDescription;
    private String techStack;
    private String location;
    private String etc;
    private Long leaderId;
    private Timestamp createDate;
    private List<String> studyDays; // 스터디 요일 및 시간 리스트

    public StudyResponseDTO(){}

    // entity => dto
    public StudyResponseDTO(Study study) {
        this.studyId = study.getStudyId();
        this.studyName = study.getStudyName();
        this.studyType = study.getStudyType();
        this.maxMembers = study.getMaxMembers();
        this.studyDescription = study.getStudyDescription();
        this.techStack = study.getTechStack();
        this.location = study.getLocation();
        this.etc = study.getEtc();
        this.leaderId = study.getLeaderId();
        this.createDate = study.getCreatedDate();
        this.studyDays = study.getStudyDays();
    }

}
