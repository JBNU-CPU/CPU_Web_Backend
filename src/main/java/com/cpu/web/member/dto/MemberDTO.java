package com.cpu.web.member.dto;

import com.cpu.web.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MemberDTO {

    private Long id;
    private String username;
    private String nickName;
    private Member.Role role;  // Role enum 타입으로 변경
    private String personName;
    private String email;
    private Timestamp createDate;
    private Timestamp updateDate;

    // entity => dto 변환
    public MemberDTO(Member member) {
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
