package com.cpu.web.member.dto.response;

import com.cpu.web.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

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
    private String phone;  // 전화번호 필드 추가
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
        this.phone = member.getPhone();  // 전화번호도 dto에 설정
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }
}
