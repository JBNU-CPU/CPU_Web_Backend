package com.cpu.web.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageEditDTO {
    @NotEmpty
    private String nickName;

    @NotEmpty
    private String personName;

    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty  // 전화번호 필드 추가 및 유효성 검사
    private String phone;
}
