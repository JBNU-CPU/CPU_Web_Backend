package com.cpu.web.board.dto.response;

import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "댓글 작성 요청 데이터")
public class CommentRequestDTO {

    private Long postId;
    private String content;

    // dto -> entity
    public Comment toCommentEntity(Post post){
        Comment comment = new Comment();

        comment.setPost(post);
        comment.setContent(content);

        return comment;
    }
}
