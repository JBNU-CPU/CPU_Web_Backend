package com.cpu.web.board.controller;

import com.cpu.web.board.dto.CommentDTO;
import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping()
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO){
        Comment comment = commentService.createComment(commentDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/post/comment/{id}")
                .buildAndExpand(comment.getCommentId())
                .toUri();
        return ResponseEntity.created(location).body(commentDTO);
    }
    
    // 특정 글의 모든 댓글 조회
    @GetMapping("/{id}")
    public List<CommentDTO> getAllComments(@PathVariable Long id) {
        return commentService.getAllComments(id);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateBulletinComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBulletinComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
