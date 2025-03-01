package com.cpu.web.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordDTO {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
