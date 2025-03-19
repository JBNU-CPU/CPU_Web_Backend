package com.cpu.web.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청 데이터")
public class SignupDTO {

    @Schema(description = "사용자명(학번)", example = "202312345", required = true)
    @NotEmpty(message = "사용자명(ID)은 필수입니다.")
    @Pattern(regexp = "^20\\d{7}$", message = "사용자명(ID)은 학번 형식(20XXXXXXX)이어야 합니다.")
    private String username;

    @Schema(description = "비밀번호", example = "P@ssw0rd!", required = true)
    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 100, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String password;

    @Schema(description = "닉네임", example = "coolguy123", required = true)
    @NotEmpty(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 30, message = "닉네임은 최소 2자 이상, 최대 30자까지 가능합니다.")
    private String nickName;

    @Schema(description = "이름", example = "홍길동", required = true)
    @NotEmpty(message = "이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "이름은 최소 2자 이상, 최대 50자까지 가능합니다.")
    private String personName;

    @Schema(description = "이메일 주소", example = "user@example.com", required = true)
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;

    @Schema(description = "전화번호", example = "010-1234-5678", required = true)
    @NotEmpty(message = "전화번호는 필수입니다.")
    @Size(min = 10, max = 15, message = "전화번호는 최소 10자 이상, 최대 15자까지 가능합니다.")
    private String phone;
}
