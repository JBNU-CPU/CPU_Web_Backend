package com.cpu.web.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "아이디 및 이메일 중복 확인 요청 데이터")
public class CheckDTO {

    @Schema(description = "사용자명(학번)", example = "202312345", required = true)
    @NotEmpty(message = "사용자명(ID)은 필수입니다.")
    @Pattern(regexp = "^20\\d{7}$", message = "사용자명(ID)은 학번 형식(20XXXXXXX)이어야 합니다.")
    private String username;

    @Schema(description = "이메일 주소", example = "user@example.com", required = true)
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;
}
