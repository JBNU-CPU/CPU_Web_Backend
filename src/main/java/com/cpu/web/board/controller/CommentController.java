package com.cpu.web.board.controller;

import com.cpu.web.board.dto.CommentDTO;
import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "댓글 작성", description = "댓글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "Long", example = "123"))),
            @Parameter(name = "comment", description = "댓글 내용", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "퍼가용~"))),
    })
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
    @Operation(summary = "특정 글의 모든 댓글 조회", description = "특정 글 모든 댓글 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "Long", example = "123"))),
            @Parameter(name = "comment", description = "댓글 내용", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "퍼가용~"))),
    })
    public List<CommentDTO> getAllComments(@PathVariable Long id) {
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
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
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
