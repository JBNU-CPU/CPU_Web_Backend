package com.cpu.web.board.dto;

import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {

    private Long postId;
    private String content;

    public CommentDTO(){}

    // entity => dto
    public CommentDTO(Comment comment) {
        this.content = comment.getContent();
        this.postId = comment.getPost().getPostId();
    }

    // dto => entity
    public Comment toCommentEntity(String content, Post post){
        Comment comment = new Comment();

        comment.setContent(content);
        comment.setPost(post);

        return comment;
    }
}