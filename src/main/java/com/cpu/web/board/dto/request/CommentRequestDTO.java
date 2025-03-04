package com.cpu.web.board.dto.request;

import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.entity.Post;
import com.cpu.web.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "댓글 작성 요청 데이터")
public class CommentRequestDTO {

    private Long postId;
    private String content;

    // dto => entity
    public Comment toCommentEntity(String content, Post post, Member member){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setMember(member);
        return comment;
    }
}
