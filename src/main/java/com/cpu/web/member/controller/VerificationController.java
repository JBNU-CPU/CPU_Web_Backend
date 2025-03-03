package com.cpu.web.member.controller;

import com.cpu.web.member.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/send-code")
    @Operation(summary = "이메일 인증 코드 전송", description = "사용자 이메일로 인증 코드를 전송하고 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 코드가 성공적으로 전송되었습니다."),
            @ApiResponse(responseCode = "500", description = "이메일 전송 실패")
    })
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        try {
            String responseMessage = verificationService.sendAndSaveVerificationCode(email);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("이메일 전송 과정에서 문제가 발생했습니다.");
        }
    }

    @PostMapping("/verify-code")
    @Operation(summary = "인증 코드 확인", description = "제공된 이메일과 인증 코드를 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증이 성공적으로 완료되었습니다. 다음 단계로 진행할 수 있습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 인증 코드입니다.")
    })
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code, HttpSession session) {
        if (verificationService.verifyCode(email, code)) {
            session.setAttribute("verifiedUser", email);
            return ResponseEntity.ok("인증이 성공적으로 완료되었습니다. 다음 단계로 진행하세요.");
        } else {
            return ResponseEntity.badRequest().body("잘못된 인증 코드입니다.");
        }
    }
}
