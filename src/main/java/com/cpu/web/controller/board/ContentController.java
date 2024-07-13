package com.cpu.web.controller.board;

import com.cpu.web.dto.BulletinDTO;
import com.cpu.web.service.BulletinService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// 자유 게시판 컨트롤러
@RestController
@RequestMapping("/content")
public class ContentController {

    private final BulletinService bulletinService;

    public ContentController(BulletinService bulletinService){
        this.bulletinService = bulletinService;
    }


    // 자유 게시판 페이지
    @GetMapping
    public String content(Model model) {

        return "content";
    }

    //  자유 글 작성
    @PostMapping
    public String createContent(@RequestBody BulletinDTO bulletinDTO) {

        boolean isSaved = bulletinService.createContent(bulletinDTO);
        System.out.println(isSaved);

        return  "content";
    }
}
