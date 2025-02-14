package com.cpu.web.scholarship.dto.response;

import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.entity.Study;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class StudyResponseDTO {
    private Long studyId;
    private Boolean isAccepted;
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
    private List<MemberStudyDTO> memberStudies;

    public StudyResponseDTO(){}

    // TODO: DB에 List<String> 형태로 저장되어 있는 studyDays 필드 파싱해서 List<{요일, 시작시간, 종료시간}>으로 만들기
    // entity => dto (스터디 참여 멤버 정보 없음)
    public StudyResponseDTO(Study study) {
        this.studyId = study.getStudyId();
        this.isAccepted = study.getIsAccepted();
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

    // entity => dto (스터디 참여 멤버 정보 있음)
    public StudyResponseDTO(Study study, List<MemberStudy> memberStudies) {
        this.studyId = study.getStudyId();
        this.isAccepted = study.getIsAccepted();
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
        this.memberStudies = memberStudies.stream()
                .map(MemberStudyDTO::new)
                .collect(Collectors.toList());
    }

}
