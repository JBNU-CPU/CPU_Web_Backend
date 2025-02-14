package com.cpu.web.scholarship.dto.request;

import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.entity.Study;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class StudyRequestDTO {
    private String studyName;
    private String studyType;
    private int maxMembers;
    private String studyDescription;
    private String techStack;
    private String location;
    private String etc;
    private List<StudyScheduleDTO> studyDays; // 스터디 요일 및 시간 리스트
    private List<MemberInfo> members; // 참여 멤버 리스트

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

    @Getter
    @Setter
    public static class StudyScheduleDTO {
        private String day;
        private String startTime;
        private String endTime;

        public String toScheduleString() {
            return String.format("%s %s-%s", day, startTime, endTime);
        }
    }
    
    public Study toStudyEntity() {
        Study study = new Study();
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
        return study;
    }
}
