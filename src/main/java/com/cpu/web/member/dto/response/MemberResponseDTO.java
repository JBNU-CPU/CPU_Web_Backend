package com.cpu.web.member.dto.response;

import com.cpu.web.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class MemberResponseDTO {

    private Long id;
    private String username;
    private String nickName;
    private Member.Role role;  // Role enum 타입으로 변경
    private String personName;
    private String email;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    // entity => dto 변환
    public MemberResponseDTO(Member member) {
        this.id = member.getMemberId();
        this.username = member.getUsername();
        this.nickName = member.getNickName();
        this.role = member.getRole();  // Role enum을 직접 사용
        this.personName = member.getPersonName();
        this.email = member.getEmail();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }
}
