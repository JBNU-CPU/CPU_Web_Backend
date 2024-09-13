package com.cpu.web.board.controller;

// 게시판 컨트롤러

import com.cpu.web.board.dto.PostDTO;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    //글 작성
    @PostMapping
    public ResponseEntity<PostDTO> createBulletin(@RequestBody PostDTO postDTO) {
        Post post = postService.createPost(postDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/bulletin/{id}")
                .buildAndExpand(post.getPostId())
                .toUri();
        return ResponseEntity.created(location).body(postDTO);
    }

    // 페이징 처리된 전체 글 조회
    @GetMapping
    public Page<PostDTO> getAllBulletins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return postService.getAllPosts(page, size);
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getBulletinById(@PathVariable Long id) {
        Optional<PostDTO> bulletinDTO = postService.getPostById(id);
        return bulletinDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 글 수정
    @PutMapping("/{id}")
    public  ResponseEntity<PostDTO> updateBulletin(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        PostDTO updatedPostDTO = postService.updatePost(id, postDTO);
        return ResponseEntity.ok(updatedPostDTO);
    }

    // 글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBulletin(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
