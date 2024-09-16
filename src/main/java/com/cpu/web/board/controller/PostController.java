package com.cpu.web.board.controller;

// 게시판 컨트롤러

import com.cpu.web.board.dto.PostDTO;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Board", description = "게시판 API")
public class PostController {

    private final PostService postService;
    //글 작성
    @PostMapping
    @Operation(summary = "게시글 작성", description = "게시글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "isNotice", description = "공지 여부", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "boolean", example = "true"))),
            @Parameter(name = "title", description = "제목", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "박관소 개최 안내"))),
            @Parameter(name = "content", description = "내용", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "2024년 하반기 박관소가 개최 예정입니다.")))
    })
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        Post post = postService.createPost(postDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/post/{id}")
                .buildAndExpand(post.getPostId())
                .toUri();
        return ResponseEntity.created(location).body(postDTO);
    }

    // 페이징 처리된 전체 글 조회
    @GetMapping
    @Operation(summary = "게시글 전체 조회", description = "게시글 전체 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public Page<PostDTO> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return postService.getAllPosts(page, size);
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    @Operation(summary = "게시글 개별 조회", description = "게시글 개별 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다..", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        Optional<PostDTO> postDTO = postService.getPostById(id);
        return postDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 글 수정
    @PutMapping("/{id}")
    @Operation(summary = "게시글 수성", description = "게시글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "title", description = "제목", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "박관소 개최 안내"))),
            @Parameter(name = "email", description = "이메일", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "2024년 하반기 박관소가 개최 예정입니다.")))
    })
    public  ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        PostDTO updatedPostDTO = postService.updatePost(id, postDTO);
        return ResponseEntity.ok(updatedPostDTO);
    }

    // 글 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
