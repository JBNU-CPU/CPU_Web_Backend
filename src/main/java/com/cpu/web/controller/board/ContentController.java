package com.cpu.web.controller.board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// 자유 게시판 컨트롤러
@Controller
public class ContentController {

    // 자유 게시판 페이지
    @GetMapping("/content")
    public String content(Model model) {

        return "content";
    }
}
