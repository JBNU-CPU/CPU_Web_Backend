package com.cpu.web.board.dto.response;


import com.cpu.web.board.entity.Gathering;
import com.cpu.web.board.entity.MemberGathering;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Schema(description = "소모임 응답 데이터")
public class GatheringResponseDTO {

    @Schema(description = "소모임 ID", example = "1")
    private Long id;

    @Schema(description = "개설자 id", example = "1")
    private Long leaderId;

    @Schema(description = "개설자 학번", example = "202018556")
    private String leaderUserName;

    @Schema(description = "개설자 이름", example = "박도현")
    private String leaderName;

    @Schema(description = "개설자 전화번호", example = "010-1234-5678")
    private String leaderPhone;

    @Schema(description = "소모임명", example = "보드게임 소모임")
    private String gatheringTitle;

    @Schema(description = "소모임 설명", example = "일주일에 한 번 보드게임을 즐기는 소모임입니다.")
    private String gatheringContent;

    @Schema(description = "소모임 기타", example = "아무나 환영합니다~")
    private String etc;

    @Schema(description = "스터디 최대 인원", example = "8")
    private Integer maxMembers;

    @Schema(description = "현재 참여 인원 수", example = "5")
    private Long currentCount;

    @Schema(description = "생성일", example = "2025-01-22T11:45:33.183+00:00")
    private Timestamp createDate;

    @Schema(description = "소모임 스케쥴", example = "[\n" +
            "                \"Monday 10:00-24:00\",\n" +
            "                \"Tuesday 14:00-24:00\"\n" +
            "            ]")
    private List<String> gatheringDays; // 스터디 요일 및 시간 리스트

    // entity => dto (소모임 참여 멤버 정보 없음)
    public GatheringResponseDTO(Gathering gathering){
        this.id = gathering.getGatheringId();
        this.leaderId = gathering.getMember().getMemberId();
        this.leaderName = gathering.getMember().getPersonName();
        this.leaderUserName = gathering.getMember().getUsername();
        this.leaderPhone = gathering.getMember().getPhone();
        this.gatheringTitle = gathering.getGatheringTitle();
        this.gatheringContent = gathering.getGatheringContent();
        this.etc = gathering.getEtc();
        this.maxMembers = gathering.getMaxMembers();
        this.createDate = gathering.getCreatedDate();
        this.gatheringDays = gathering.getGatheringDays();
        this.currentCount = 0L; // 기본값
    }

    // entity => dto 소모임 참여 멤버 정보 없음, 현재 참여 인원수 포함
    public GatheringResponseDTO(Gathering gathering, Long currentCount){
        this.id = gathering.getGatheringId();
        this.leaderId = gathering.getMember().getMemberId();
        this.leaderName = gathering.getMember().getPersonName();
        this.leaderUserName = gathering.getMember().getUsername();
        this.leaderPhone = gathering.getMember().getPhone();
        this.gatheringTitle = gathering.getGatheringTitle();
        this.gatheringContent = gathering.getGatheringContent();
        this.etc = gathering.getEtc();
        this.maxMembers = gathering.getMaxMembers();
        this.createDate = gathering.getCreatedDate();
        this.gatheringDays = gathering.getGatheringDays();
        this.currentCount = currentCount; // 기본값
    }

    // entity => dto (스터디 참여 멤버 정보 있음)
    public GatheringResponseDTO(Gathering gathering, List<MemberGathering> memberGatherings, Long currentCount) {

    }
}
