package com.cpu.web.board.controller;

import com.cpu.web.board.dto.request.CommentRequestDTO;
import com.cpu.web.board.dto.response.CommentResponseDTO;
import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.repository.PostRepository;
import com.cpu.web.board.service.CommentService;
import com.cpu.web.board.service.PostService;
import com.cpu.web.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final PostRepository postRepository;

    // 댓글 작성
    @PostMapping()
    @Operation(summary = "댓글 작성", description = "댓글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "Long", example = "123"))),
            @Parameter(name = "comment", description = "댓글 내용", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "퍼가용~"))),
    })
    public ResponseEntity<CommentRequestDTO> createComment(@RequestBody CommentRequestDTO commentRequestDTO){
        Comment comment = commentService.createComment(commentRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/post/comment/{id}")
                .buildAndExpand(comment.getCommentId())
                .toUri();
        return ResponseEntity.created(location).body(commentRequestDTO);
    }
    
    // 특정 글의 모든 댓글 조회
    @GetMapping("/{id}")
    @Operation(summary = "특정 글의 모든 댓글 조회", description = "특정 글 모든 댓글 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "Long", example = "123"))),
            @Parameter(name = "comment", description = "댓글 내용", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "퍼가용~"))),
    })
    public List<CommentResponseDTO> getAllComments(@PathVariable Long id) {
        return commentService.getAllComments(id);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정", description = "댓글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "Long", example = "123"))),
            @Parameter(name = "comment", description = "댓글 내용", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "퍼가용~"))),
    })
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long id, @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO updatedComment = commentService.updateComment(id, commentRequestDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
