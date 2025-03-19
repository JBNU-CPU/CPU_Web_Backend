package com.cpu.web.scholarship.dto.request;

import com.cpu.web.member.entity.Member;
import com.cpu.web.scholarship.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "스터디 생성 요청 데이터")
public class StudyRequestDTO {
    @Schema(description = "스터디명", example = "AI 연구 스터디", required = true)
    @NotEmpty(message = "스터디명은 필수입니다.")
    @Size(min = 1, max = 100, message = "스터디명은 최대 100자까지 가능합니다.")
    private String studyName;

    @Schema(description = "스터디 유형", example = "study", required = true)
    @NotEmpty(message = "스터디 유형은 필수입니다.")
    private String studyType;

    @Schema(description = "최대 참여 가능 인원", example = "10", required = true)
    @NotNull(message = "최대 인원은 필수입니다.")
    @Min(value = 1, message = "최소 한 명 이상이어야 합니다.")
    @Max(value = 100, message = "최대 인원은 100명까지 가능합니다.")
    private Integer maxMembers;

    @Schema(description = "스터디 설명", example = "AI 연구 및 논문 리뷰를 진행하는 스터디입니다.", required = true)
    @NotEmpty(message = "스터디 설명은 필수입니다.")
    @Size(min = 1, max = 10000, message = "스터디 설명은 최대 10000자까지 가능합니다.")
    private String studyDescription;

    @Schema(description = "기술 스택", example = "Python, TensorFlow", required = true)
    @NotEmpty(message = "기술 스택은 필수입니다.")
    @Size(min = 1, max = 1000, message = "기술 스택은 최대 1000자까지 가능합니다.")
    private String techStack;

    @Schema(description = "스터디 위치", example = "중앙도서관 그룹학습실", required = true)
    @NotEmpty(message = "스터디 위치는 필수입니다.")
    @Size(min = 1, max = 1000, message = "스터디 위치는 최대 1000자까지 가능합니다.")
    private String location;

    @Schema(description = "기타 정보", example = "온라인 진행 가능")
    private String etc;

    private List<StudyScheduleDTO> studyDays; // 스터디 요일 및 시간 리스트

    @Getter
    @Setter
    public static class StudyScheduleDTO {
        @NotEmpty(message = "요일은 필수입니다.")
        private String day;

        @NotEmpty(message = "시작 시간은 필수입니다.")
        private String startTime;

        @NotEmpty(message = "종료 시간은 필수입니다.")
        private String endTime;

        public String toScheduleString() {
            return String.format("%s %s-%s", day, startTime, endTime);
        }
    }

    public Study toStudyEntity(Member member) {
        Study study = new Study();
        study.setStudyName(this.studyName);
        study.setStudyType(Study.StudyType.valueOf(this.studyType.toLowerCase()));
        study.setMaxMembers(this.maxMembers);
        study.setStudyDescription(this.studyDescription);
        study.setTechStack(this.techStack);
        study.setLocation(this.location);
        study.setEtc(this.etc);
        study.setLeader(member);

        // studyDays 타입 List<StudyScheduleDTO> -> List<String> 변환
        if(this.studyDays != null) {
            List<String> scheduleStrings = this.studyDays.stream()
                    .map(StudyScheduleDTO::toScheduleString)
                    .collect(Collectors.toList());
            study.setStudyDays(scheduleStrings); // 변환 후 엔티티에 저장
        }
        return study;
    }

    // 스터디 수정 (기존 관계가 끊어지지 않도록)
    public void updateStudyEntity(Study study) {
        study.setStudyName(this.studyName);
        study.setStudyType(Study.StudyType.valueOf(this.studyType.toLowerCase()));
        study.setMaxMembers(this.maxMembers);
        study.setStudyDescription(this.studyDescription);
        study.setTechStack(this.techStack);
        study.setLocation(this.location);
        study.setEtc(this.etc);

        // studyDays 타입 List<StudyScheduleDTO> -> List<String> 변환
        if(this.studyDays != null) {
            List<String> scheduleStrings = this.studyDays.stream()
                    .map(StudyScheduleDTO::toScheduleString)
                    .collect(Collectors.toList());
            study.setStudyDays(scheduleStrings); // 변환 후 엔티티에 저장
        }
    }

}
