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
}
