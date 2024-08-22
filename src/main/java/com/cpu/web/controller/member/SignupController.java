package com.cpu.web.controller.member;

import com.cpu.web.dto.member.SignupDTO;
import com.cpu.web.service.member.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

//    @GetMapping("/signup")
//    public String signupP() {
//        return "signup";
//    }

    @PostMapping("/signup")
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
