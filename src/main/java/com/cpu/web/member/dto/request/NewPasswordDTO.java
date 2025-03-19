package com.cpu.web.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "비밀번호 변경 요청 데이터")
public class NewPasswordDTO {

    @Schema(description = "사용자명(아이디)", example = "user123", required = true)
    @NotEmpty(message = "사용자명은 필수입니다.")
    private String username;

    @Schema(description = "이메일 주소", example = "user@example.com", required = true)
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;

    @Schema(description = "새 비밀번호", example = "P@ssw0rd!", required = true)
    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 100, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String password;
}
