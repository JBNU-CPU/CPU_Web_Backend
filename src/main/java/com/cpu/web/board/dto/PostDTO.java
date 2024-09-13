package com.cpu.web.board.dto;

import com.cpu.web.board.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {

    private Boolean isNotice;
    private String  title;
    private String content;

    public PostDTO(){}


    // entity => dto
    public PostDTO(Post post) {
        this.isNotice = post.getIsNotice();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    // dto => entity
    public Post toPostEntity(){
        Post post = new Post();
        post.setIsNotice(isNotice);
        post.setTitle(title);
        post.setContent(content);
        return post;
    }


}