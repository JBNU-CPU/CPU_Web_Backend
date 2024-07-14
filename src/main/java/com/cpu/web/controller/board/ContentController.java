package com.cpu.web.controller.board;

import com.cpu.web.dto.board.BulletinDTO;
import com.cpu.web.service.board.BulletinService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 자유 게시판 컨트롤러
@RestController
@RequestMapping("/bulletin")
public class ContentController {

    private final BulletinService bulletinService;

    public ContentController(BulletinService bulletinService){
        this.bulletinService = bulletinService;
    }

    //글 작성
    @PostMapping
    public void createContent(@RequestBody BulletinDTO bulletinDTO) {

        boolean isSaved = bulletinService.createBulletin(bulletinDTO);
        System.out.println(isSaved);

    }

    //글 전체 조회
    @GetMapping
    public List<BulletinDTO> getAllBulletins() {

        return  bulletinService.getAllBulletin();
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    public BulletinDTO getBulletinById(@PathVariable Long id) {
        return bulletinService.getBulletinById(id);
    }

    // 글 수정
    @PutMapping("/{id}")
    public void updateBulletin(@PathVariable Long id, @RequestBody BulletinDTO bulletinDTO){
        bulletinService.updateBulletin(id, bulletinDTO);
    }

    // 글 삭제
    @DeleteMapping("/{id}")
    public void deleteBulletin(@PathVariable Long id){
        bulletinService.deleteBulletin(id);
    }
}
