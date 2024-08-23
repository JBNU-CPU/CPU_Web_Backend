package com.cpu.web.dto.member;

import com.cpu.web.entity.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MyPageDTO {
    private Long id;
    private String username;
    private String personName;
    private String role;
    private Timestamp createDate;
    private Timestamp updateDate;

    // 기본 생성자
    public MyPageDTO() {}

    // 엔티티를 DTO로 변환하는 생성자
    public MyPageDTO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.personName = member.getPersonName();
        this.role = member.getRole();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }
}
