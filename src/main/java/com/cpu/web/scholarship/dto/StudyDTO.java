package com.cpu.web.scholarship.dto;

import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.entity.Study;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class StudyDTO {
    private Long id;
    private Long leaderId;
    private Boolean isAccepted;
    private String studyName;
    private String studyType;
    private int maxMembers;
    private String studyDescription;
    private String techStack;
    private List<String> studyDays;
    private String location;
    private String etc;

    // ✅ 추가: 참여 멤버 리스트
    private List<MemberInfo> members;

    @Getter
    @Setter
    public static class MemberInfo {
        private Long memberId;
        private Boolean isLeader;

        public MemberInfo(MemberStudy memberStudy) {
            this.memberId = memberStudy.getMember().getMemberId();
            this.isLeader = memberStudy.getIsLeader();
        }
    }

    // ✅ 기본 생성자
    public StudyDTO() {}

    // ✅ 멤버 정보 없이 Study → StudyDTO 변환
    public StudyDTO(Study study) {
        this.id = study.getStudyId();
        this.leaderId = study.getLeaderId();
        this.isAccepted = study.getIsAccepted();
        this.studyName = study.getStudyName();
        this.studyType = study.getStudyType().name();
        this.maxMembers = study.getMaxMembers();
        this.studyDescription = study.getStudyDescription();
        this.techStack = study.getTechStack();
        this.location = study.getLocation();
        this.etc = study.getEtc();
        this.members = List.of(); // ✅ 멤버 정보 없을 경우 빈 리스트로 초기화
    }

    // ✅ Study 엔티티 → StudyDTO 변환 (멤버 리스트 포함)
    public StudyDTO(Study study, List<MemberStudy> memberStudies) {
        this.id = study.getStudyId();
        this.leaderId = study.getLeaderId();
        this.isAccepted = study.getIsAccepted();
        this.studyName = study.getStudyName();
        this.studyType = study.getStudyType().name();
        this.maxMembers = study.getMaxMembers();
        this.studyDescription = study.getStudyDescription();
        this.techStack = study.getTechStack();
        this.location = study.getLocation();
        this.etc = study.getEtc();
        this.members = memberStudies.stream().map(MemberInfo::new).collect(Collectors.toList()); // ✅ 멤버 리스트 포함
    }

    // ✅ StudyDTO → Study 변환
    public Study toStudyEntity() {
        Study study = new Study();
        study.setStudyId(this.id);
        study.setLeaderId(this.leaderId);
        study.setStudyName(this.studyName);
        study.setStudyType(Study.StudyType.valueOf(this.studyType.toLowerCase()));
        study.setMaxMembers(this.maxMembers);
        study.setStudyDescription(this.studyDescription);
        study.setTechStack(this.techStack);
        study.setLocation(this.location);
        study.setEtc(this.etc);
        return study;
    }
}
