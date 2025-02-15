package com.cpu.web.member.controller;

import com.cpu.web.member.dto.response.SignupDTO;
import com.cpu.web.member.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 API")
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "회원 가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "요청에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "username", description = "학번", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "202018556"))),
            @Parameter(name = "password", description = "비밀번호", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "password", example = "qwer1234"))),
            @Parameter(name = "nickname", description = "닉네임", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "nick123"))),
            @Parameter(name = "personName", description = "이름", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "홍길동"))),
            @Parameter(name = "email", description = "이메일", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "email", example = "user@example.com")))
    })
    public ResponseEntity<?> signupProcess(SignupDTO signupDTO) {
        try {
            Long memberId = signupService.signup(signupDTO);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(memberId)
                    .toUri();
            return ResponseEntity.created(location).body("회원 가입 성공");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}