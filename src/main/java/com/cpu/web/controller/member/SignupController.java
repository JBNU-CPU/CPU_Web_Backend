package com.cpu.web.controller.member;

import com.cpu.web.dto.member.SignupDTO;
import com.cpu.web.service.member.SignupService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @GetMapping("/signup")
    public String signupP() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signupProcess(SignupDTO signupDTO) {
        System.out.println("signupDTO.getUsername() = " + signupDTO.getUsername());
        System.out.println("signupDTO.getPassword() = " + signupDTO.getPassword());
        signupService.signup(signupDTO);

        return "redirect:/login";
    }
}
