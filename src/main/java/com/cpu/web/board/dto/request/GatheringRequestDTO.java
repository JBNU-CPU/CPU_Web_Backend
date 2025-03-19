package com.cpu.web.board.dto.request;

import com.cpu.web.board.entity.Gathering;
import com.cpu.web.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Schema(description = "소모임 생성 요청 데이터")
public class GatheringRequestDTO {

    @Schema(description = "소모임 제목", example = "기술 교류 모임", required = true)
    @NotEmpty(message = "제목은 필수입니다.")
    @Size(min = 1, max = 100, message = "제목은 최대 100자까지 가능합니다.")
    private String title;

    @Schema(description = "소모임 내용", example = "다양한 기술에 대해 토론하고 공유하는 모임입니다.", required = true)
    @NotEmpty(message = "내용은 필수입니다.")
    @Size(min = 1, max = 10000, message = "내용은 최대 10000자까지 가능합니다.")
    private String content;

    @Schema(description = "최대 참여 가능 인원", example = "50", required = true)
    @NotNull(message = "최대 인원은 필수입니다.")
    @Min(value = 1, message = "최소 한 명 이상이어야 합니다.")
    @Max(value = 100, message = "최대 인원은 100명까지 가능합니다.")
    private Integer maxMembers;

    @Schema(description = "기타 정보", example = "온라인으로 진행됩니다.")
    private String etc;

    private List<GatheringScheduleDTO> gatheringDays;

    @Getter
    @Setter
    public static class GatheringScheduleDTO {
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

    public Gathering toGatheringEntity(Member member) {
        Gathering gathering = new Gathering();
        gathering.setGatheringTitle(title);
        gathering.setGatheringContent(content);
        gathering.setMaxMembers(maxMembers);
        gathering.setEtc(etc);
        gathering.setLeader(member);
        // 소모임 요일과 시간을 문자열 리스트로 변환하여 저장
        if(this.gatheringDays != null) {
            List<String> scheduleStrings = this.gatheringDays.stream()
                    .map(GatheringScheduleDTO::toScheduleString)
                    .collect(Collectors.toList());
            gathering.setGatheringDays(scheduleStrings);
        }
        return gathering;
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
