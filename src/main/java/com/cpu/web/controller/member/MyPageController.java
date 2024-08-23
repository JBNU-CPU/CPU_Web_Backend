package com.cpu.web.controller.member;

import com.cpu.web.dto.member.MyPageDTO;
import com.cpu.web.service.member.MyPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    // 마이페이지 유저 정보 조회
    @GetMapping("/profile")
    public String getUserProfile(Model model) {
        MyPageDTO profile = myPageService.getUserProfile();
        model.addAttribute("profile", profile);
        return "profile";
    }

    // 유저 정보 수정
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute MyPageDTO myPageDTO) {
        myPageService.updateProfile(myPageDTO);
        return "redirect:/profile";
    }

    // 회원 탈퇴 페이지 이동
    @GetMapping("/withdrawal")
    public String getWithdrawalPage() {
        return "withdrawal";
    }

    // 유저 정보 삭제 (회원 탈퇴)
    @PostMapping("/withdrawal")
    public String withdrawl(@RequestParam String password) {
        boolean isWithdrawn = myPageService.withdrawl(password);
        if (isWithdrawn) {
            return "redirect:/login";
        } else {
            return "redirect:/withdrawal";
        }
    }
}
