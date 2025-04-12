package com.cpu.web.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인 요청 데이터")
public class LoginDTO {

    @Schema(description = "사용자명(학번)", example = "202312345", required = true)
    @NotEmpty(message = "사용자명(ID)은 필수입니다.")
    @Pattern(regexp = "^20\\d{7}$", message = "사용자명(ID)은 학번 형식(20XXXXXXX)이어야 합니다.")
    private String username;

    @Schema(description = "비밀번호", example = "P@ssw0rd!", required = true)
    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 100, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
}
