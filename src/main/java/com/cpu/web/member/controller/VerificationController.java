package com.cpu.web.member.controller;


import com.cpu.web.member.service.MailService;
import com.cpu.web.member.service.VerificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class VerificationController {

    private final MailService mailService;
    private final VerificationService verificationService;

    public VerificationController(MailService mailService, VerificationService verificationService) {
        this.mailService = mailService;
        this.verificationService = verificationService;
    }

    // 이메일 인증 코드 전송
    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        String verificationCode = mailService.sendVerificationCode(email);
        verificationService.saveVerificationCode(email, verificationCode);
        return ResponseEntity.ok("Verification code sent.");
    }

    // 인증 코드 확인
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code, HttpSession session) {
        if (verificationService.verifyCode(email, code)) {
            // 인증이 성공하면 세션에 인증된 사용자 정보를 저장
            session.setAttribute("verifiedUser", email);
            return ResponseEntity.ok("Verification successful. You can now proceed to the next step.");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification code.");
        }
    }

}
