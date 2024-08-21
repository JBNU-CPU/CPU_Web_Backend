package com.cpu.web.controller.member;

import com.cpu.web.dto.member.CustomMember;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String admin(Authentication authentication) {
        CustomMember customMember = (CustomMember) authentication.getPrincipal();
        System.out.println("로그인 아이디 : " + customMember.getUsername());
        return "admin";
    }
    
    //TODO: 유저 조회, 변경, 삭제
    //TODO: 스터디 신청자 조회, 변경, 삭제
}
