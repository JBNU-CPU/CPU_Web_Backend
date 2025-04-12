package com.cpu.web.board.dto.request;

import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.entity.Post;
import com.cpu.web.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "댓글 작성 요청 데이터")
public class CommentRequestDTO {

    @Schema(description = "게시글 ID", example = "1", required = true)
    @NotNull(message = "게시글 ID는 필수입니다.")
    private Long postId;

    @Schema(description = "댓글 내용", example = "정말 유익한 글이네요!", required = true)
    @NotEmpty(message = "내용은 필수입니다.")
    @Size(min = 1, max = 10000, message = "내용은 최대 10000자까지 가능합니다.")
    private String content;

    // dto => entity
    public Comment toCommentEntity(String content, Post post, Member member){
        Comment comment = new Comment();
        comment.setContent(this.content);
        comment.setPost(post);
        comment.setMember(member);
        return comment;
    }
}
