package com.cpu.web.board.dto.request;

import com.cpu.web.board.entity.Gathering;
import com.cpu.web.member.entity.Member;
import com.cpu.web.scholarship.dto.request.StudyRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GatheringRequestDTO {

    private String title;
    private String content;
    private Integer maxMembers;
    private String etc;
    private List<GatheringScheduleDTO> gatheringDays;

    @Getter
    @Setter
    public static class GatheringScheduleDTO {
        private String day;
        private String startTime;
        private String endTime;

        public String toScheduleString() {
            return String.format("%s %s-%s", day, startTime, endTime);
        }
    }

    public Gathering toGatheringEntity(Member member) {
        Gathering gathering = new Gathering();
        gathering.setGatheringTitle(title);
        gathering.setGatheringContent(content);
        gathering.setMaxMembers(maxMembers);
        gathering.setEtc(etc);
        gathering.setLeader(member);

        // gatheringDays 타입 List<GatheringSchedule> -> List<String> 변환
        if(this.gatheringDays != null) {
            List<String> scheduleStrings = this.gatheringDays.stream()
                    .map(GatheringScheduleDTO::toScheduleString)
                    .collect(Collectors.toList());

            // 변환 값 확인
            System.out.println("Converted gatheringDays: " + scheduleStrings);

            gathering.setGatheringDays(scheduleStrings); // 변환 후 엔티티에 저장
        }else {
            System.out.println("소모임 요일이 null 값입니다.");
        }

        return gathering;  // Gathering 객체 반환
    }

    public void updateGatheringEntity(Gathering gathering) {
        gathering.setGatheringTitle(title);  // title 설정
        gathering.setGatheringContent(content);
        gathering.setMaxMembers(maxMembers);
        gathering.setEtc(etc);

        // gatheringDays 타입 List<GatheringSchedule> -> List<String> 변환
        if(this.gatheringDays != null) {
            List<String> scheduleStrings = this.gatheringDays.stream()
                    .map(GatheringScheduleDTO::toScheduleString)
                    .collect(Collectors.toList());
            gathering.setGatheringDays(scheduleStrings); // 변환 후 엔티티에 저장
        }
    }
}
