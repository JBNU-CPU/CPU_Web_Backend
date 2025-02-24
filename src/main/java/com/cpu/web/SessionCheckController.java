package com.cpu.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SessionCheckController {

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(Authentication authentication, HttpSession session) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 상태 아님");
        }
        return ResponseEntity.ok("로그인 상태 유지 중, 세션 ID: " + session.getId());
    }
}
