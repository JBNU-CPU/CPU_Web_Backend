package com.cpu.web.controller.board;

import com.cpu.web.dto.board.BulletinDTO;
import com.cpu.web.service.board.BulletinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bulletin")
public class BulletinController {

    private final BulletinService bulletinService;

    // 페이징된 전체 글 조회
    @GetMapping
    public String getAllBulletins(@RequestParam(defaultValue = "0") int page, Model model) {
        int size = 10; // 한 페이지에 10개의 게시글
        Page<BulletinDTO> bulletinPage = bulletinService.getAllBulletins(page, size);
        model.addAttribute("bulletins", bulletinPage.getContent());
        return "bulletins"; // bulletins.html 템플릿 반환
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    public String getBulletinById(@PathVariable Long id, Model model) {
        BulletinDTO bulletinDTO = bulletinService.getBulletinById(id).orElse(null);
        model.addAttribute("bulletin", bulletinDTO);
        return "bulletinDetail"; // bulletinDetail.html 템플릿 반환
    }

    // 글 저장
    @PostMapping
    public String createBulletin(@ModelAttribute BulletinDTO bulletinDTO) {
        bulletinService.createBulletin(bulletinDTO);
        return "redirect:/bulletin";
    }

    // 글 수정
    @PostMapping("/{id}")
    public String updateBulletin(@PathVariable Long id, @ModelAttribute BulletinDTO bulletinDTO) {
        bulletinService.updateBulletin(id, bulletinDTO);
        return "redirect:/bulletin/" + id;
    }

    // 글 삭제
    @PostMapping("/{id}/delete")
    public String deleteBulletin(@PathVariable Long id) {
        bulletinService.deleteBulletin(id);
        return "redirect:/bulletin";
    }

    // 게시글 등록 페이지
    @GetMapping("/new")
    public String newBulletin() {
        return "newBulletin"; // newBulletin.html 템플릿 반환
    }

    // 게시글 수정 페이지
    @GetMapping("/{id}/edit")
    public String editBulletin(@PathVariable Long id, Model model) {
        BulletinDTO bulletinDTO = bulletinService.getBulletinById(id).orElse(null);
        model.addAttribute("bulletin", bulletinDTO);
        return "editBulletin"; // editBulletin.html 템플릿 반환
    }
}
