package com.cpu.web.scholarship.dto.response;

import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "스터디 응답 데이터")
public class StudyResponseDTO {

    @Schema(description = "게시글 ID", example = "1")
    private Long id;

    @Schema(description = "등록 여부", example = "true")
    private Boolean isAccepted;

    @Schema(description = "개설자명", example = "개설자명")
    private String leaderName;

    @Schema(description = "스터디명", example = "자바를 잡아라!")
    private String studyName;

    @Schema(description = "스터디 종류", example = "session")
    private Study.StudyType studyType;

    @Schema(description = "스터디 최대 인원", example = "8")
    private int maxMembers;

    @Schema(description = "스터디 설명", example = "자바 공부해서 취뽀해봅시다.")
    private String studyDescription;

    @Schema(description = "기술 스택", example = "Java, Spring")
    private String techStack;

    @Schema(description = "스터디 장소", example = "동아리방")
    private String location;

    @Schema(description = "기타", example = "지각비 1000원")
    private String etc;

    @Schema(description = "개설자 id", example = "1")
    private Long leaderId;

    @Schema(description = "생성일", example = "2025-01-22T11:45:33.183+00:00")
    private Timestamp createDate;

    @Schema(description = "스터디 스케쥴", example = "[\n" +
            "                \"Monday 10:00-24:00\",\n" +
            "                \"Tuesday 14:00-24:00\"\n" +
            "            ]")
    private List<String> studyDays; // 스터디 요일 및 시간 리스트

    @Schema(description = "스터디 참여자", example = "1")
    private List<MemberStudyDTO> memberStudies;

    public StudyResponseDTO(){}

    // TODO: DB에 List<String> 형태로 저장되어 있는 studyDays 필드 파싱해서 List<{요일, 시작시간, 종료시간}>으로 만들기
    // entity => dto (스터디 참여 멤버 정보 없음)
    public StudyResponseDTO(Study study) {
        this.id = study.getStudyId();
        this.isAccepted = study.getIsAccepted();
        this.leaderName = study.getLeaderName();
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
        this.id = study.getStudyId();
        this.isAccepted = study.getIsAccepted();
        this.leaderName = study.getLeaderName();
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
