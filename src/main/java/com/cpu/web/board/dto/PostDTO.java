package com.cpu.web.board.dto;

import com.cpu.web.board.entity.Post;
import com.cpu.web.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

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
    public Post toPostEntity(Member member){
        Post post = new Post();
        post.setIsNotice(isNotice);
        post.setTitle(title);
        post.setContent(content);
        post.setMember(member);
        return post;
    }


}