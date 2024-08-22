package com.cpu.web.controller.board;

import com.cpu.web.dto.board.BulletinDTO;
import com.cpu.web.dto.comment.BulletinCommentDTO;
import com.cpu.web.entity.board.Bulletin;
import com.cpu.web.entity.comment.BulletinComment;
import com.cpu.web.service.board.BulletinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

// 자유 게시판 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/bulletin")
public class BulletinController {

    private final BulletinService bulletinService;

    //글 작성
    @PostMapping
    public ResponseEntity<BulletinDTO> createBulletin(@RequestBody BulletinDTO bulletinDTO) {
        Bulletin bulletin = bulletinService.createBulletin(bulletinDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/bulletin/{id}")
                .buildAndExpand(bulletin.getBulletinId())
                .toUri();
        return ResponseEntity.created(location).body(bulletinDTO);
    }

    // 전체 글 조회
    @GetMapping
    public List<BulletinDTO> getAllBulletins() {
        return bulletinService.getAllBulletins();
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    public ResponseEntity<BulletinDTO> getBulletinById(@PathVariable Long id) {
        Optional<BulletinDTO> bulletinDTO = bulletinService.getBulletinById(id);
        return bulletinDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 글 수정
    @PutMapping("/{id}")
    public  ResponseEntity<BulletinDTO> updateBulletin(@PathVariable Long id, @RequestBody BulletinDTO bulletinDTO) {
        BulletinDTO updatedBulletinDTO = bulletinService.updateBulletin(id, bulletinDTO);
        return ResponseEntity.ok(updatedBulletinDTO);
    }

    // 글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBulletin(@PathVariable Long id) {
        bulletinService.deleteBulletin(id);
        return ResponseEntity.noContent().build();
    }

    // 댓글 작성
    @PostMapping("/comment")
    public ResponseEntity<BulletinCommentDTO> createBulletinComment(@RequestBody BulletinCommentDTO bulletinCommentDTO) {
        BulletinComment bulletinComment = bulletinService.createBulletinComment(bulletinCommentDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/bulletin/comment/{id}")
                .buildAndExpand(bulletinComment.getCommentId())
                .toUri();
        return ResponseEntity.created(location).body(bulletinCommentDTO);
    }
    
    // 특정 글의 모든 댓글 조회
    @GetMapping("/{id}/comments")
    public List<BulletinCommentDTO> getAllBulletinComments(@PathVariable Long id) {
        return bulletinService.getAllBulletinComments(id);
    }

    // 특정 댓글 조회
    @GetMapping("/comment/{id}")
    public ResponseEntity<BulletinCommentDTO> getBulletinComment(@PathVariable Long id) {
        return bulletinService.getBulletinComment(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 댓글 수정
    @PutMapping("/comment/{id}")
    public ResponseEntity<BulletinCommentDTO> updateBulletinComment(@PathVariable Long id, @RequestBody BulletinCommentDTO bulletinCommentDTO) {
        BulletinCommentDTO updatedComment = bulletinService.updateBulletinComment(id, bulletinCommentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteBulletinComment(@PathVariable Long id) {
        bulletinService.deleteBulletinComment(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
