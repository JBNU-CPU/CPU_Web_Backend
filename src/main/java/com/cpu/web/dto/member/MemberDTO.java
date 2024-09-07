package com.cpu.web.dto.member;

import com.cpu.web.entity.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MemberDTO {

    private Long id;
    private String username;
    private String nickName;
    private String role;
    private String personName;
    private String email;
    private Timestamp createDate;
    private Timestamp updateDate;

    // entity => dto
    public MemberDTO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickName = member.getNickName();
        this.role = member.getRole();
        this.personName = member.getPersonName();
        this.email = member.getEmail();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }
}
