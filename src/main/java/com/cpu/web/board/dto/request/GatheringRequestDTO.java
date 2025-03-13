package com.cpu.web.board.dto.request;

import com.cpu.web.board.entity.Gathering;
import com.cpu.web.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
        gathering.setGatheringTitle(title);  // title 설정
        gathering.setGatheringContent(content);
        gathering.setMaxMembers(maxMembers);
        gathering.setEtc(etc);
        gathering.setMember(member); // member 설정 (필요한 경우)

        return gathering;  // Gathering 객체 반환
    }
}
