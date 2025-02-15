package com.cpu.web.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordDTO {
    private String username;
    private String password;
}
