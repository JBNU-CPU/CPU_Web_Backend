package com.cpu.web.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDTO {
    private String username;
    private String password;
    private String nickName;
    private String personName;
    private String email;
}
