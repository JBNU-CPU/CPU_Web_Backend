package com.cpu.web.board.controller;

// 게시판 컨트롤러

import com.cpu.web.board.dto.request.PostRequestDTO;
import com.cpu.web.board.dto.response.PageResponseDTO;
import com.cpu.web.board.dto.response.PostResponseDTO;
import com.cpu.web.board.dto.response.SearchResponseDTO;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(name = "Board", description = "게시판 API")
public class PostController {

    private final PostService postService;

    //글 작성
    @PostMapping
    @Operation(summary = "게시글 작성", description = "게시글 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class)))
    })
    public ResponseEntity<PostResponseDTO> createPost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 작성 데이터",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostRequestDTO.class))
            )@RequestBody PostRequestDTO postRequestDTO
    ) {
        Post post = postService.createPost(postRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/post/{id}")
                .buildAndExpand(post.getPostId())
                .toUri();
        return ResponseEntity.created(location).body(new PostResponseDTO(post));
    }

    // 페이징 처리된 전체 글 조회
    @GetMapping
    @Operation(summary = "게시글 전체 조회", description = "페이지네이션된 게시글 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponseDTO.class)))
    })
    public Page<PostResponseDTO> getAllPosts(@Parameter(description = "페이지 크기 (최대 100)", example = "0")@RequestParam(defaultValue = "0") int page, @Parameter(description = "페이지 크기 (최대 100)", example = "10")@RequestParam(defaultValue = "10") int size) {

        if(page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0 이상이어야 합니다.");
        }

        if(size <= 0 || size >100) {
            throw new IllegalArgumentException("페이지 크기는 1에서 100 사이여야 합니다.");
        }

        return postService.getAllPosts(page, size);
    }

    // 특정 글 조회
    @GetMapping("/{id}")
    @Operation(summary = "게시글 개별 조회", description = "게시글 ID로 게시글 개별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<PostResponseDTO> getPost(@Parameter(description = "조회할 게시글 ID", example = "1") @PathVariable Long id) {
        Optional<PostResponseDTO> postDTO = postService.getPostById(id);
        return postDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 글 수정
    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "게시글 ID로 특정 게시글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.", content = @Content(mediaType = "application/json"))
    })
    public  ResponseEntity<PostResponseDTO> updatePost(
            @Parameter(description = "수정할 게시글의 ID", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 게시글 데이터", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class))
            ) @RequestBody PostResponseDTO postResponseDTO) {
        PostResponseDTO updatedPostResponseDTO = postService.updatePost(id, postResponseDTO);

        return ResponseEntity.ok(updatedPostResponseDTO);
    }

    // 글 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "게시글 ID로 특정 게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.")
    })
    public ResponseEntity<?> deletePost(@Parameter(description = "삭제할 게시글의 ID", example = "1") @PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 글 제목 검색
    @GetMapping("/search")
    public List<SearchResponseDTO> fullTextSearchByTitle(@RequestParam(value = "title", required = false) String title){
        return postService.searchByTitle(title);
    }
}
