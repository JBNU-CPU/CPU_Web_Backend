package com.cpu.web.board.dto.response;

import com.cpu.web.board.entity.MemberGathering;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class GatheringApplyResponseDTO {

    private Long memberGatheringId;
    private Boolean isLeader;
    private Timestamp joinDate;
    private Long memberId;
    private String nickName;
    private String email;
    private String phone;

    public GatheringApplyResponseDTO (MemberGathering memberGathering) {
        this.memberGatheringId = memberGathering.getMemberGatheringId();
        this.isLeader = memberGathering.getIsLeader();
        this.joinDate = memberGathering.getJoinDate();
        this.memberId = memberGathering.getMember().getMemberId();
        this.nickName = memberGathering.getMember().getNickName();
        this.email = memberGathering.getMember().getEmail();
        this.phone = memberGathering.getMember().getPhone();
    }
}
