package com.cpu.web.board.dto.response;

import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.entity.Post;
import com.cpu.web.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "댓글 작성 응답 데이터")
public class CommentResponseDTO {

    private Long postId;
    private Long commentId;
    private String content;
    private String nickName;

    public CommentResponseDTO(){}

    // entity => dto
    public CommentResponseDTO(Comment comment) {
        this.content = comment.getContent();
        this.postId = comment.getPost().getPostId();
        this.commentId = comment.getCommentId();
        this.nickName = comment.getMember().getNickName();
    }


}