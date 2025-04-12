package com.cpu.web.member.controller;

import com.cpu.web.member.dto.request.CheckDTO;
import com.cpu.web.member.dto.request.NewPasswordDTO;
import com.cpu.web.member.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/find")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/validate-user")
    @Operation(summary = "사용자 검증 및 인증 코드 전송", description = "제공된 사용자 이름과 이메일을 검증하고, 일치할 경우 인증 코드를 이메일로 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 코드가 전송되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "일치하는 사용자 정보가 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> validateUser(@RequestBody @Valid CheckDTO checkDTO) {
        return passwordResetService.validateAndSendCode(checkDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("일치하는 사용자 정보가 없습니다."));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "비밀번호 재설정", description = "인증된 이메일 주소로 받은 코드를 확인한 후 새 비밀번호로 재설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호가 성공적으로 재설정되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "이메일 인증되지 않았거나 사용자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> resetPassword(@RequestParam @Valid NewPasswordDTO newPasswordDTO, HttpSession session) {
        return passwordResetService.resetPassword(newPasswordDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("비밀번호 재설정 중 오류가 발생했습니다."));
    }
}
