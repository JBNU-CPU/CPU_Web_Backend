package com.cpu.web.controller.board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// 공지 게시판 컨트롤러
@Controller
public class NotiController {

    // 공지 게시판 페이지
    @GetMapping("/noti")
    public String noti(Model model) {

        return "noti";
    }
}
