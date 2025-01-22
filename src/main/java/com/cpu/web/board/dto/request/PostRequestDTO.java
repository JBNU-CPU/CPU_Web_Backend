package com.cpu.web.board.dto.request;

import com.cpu.web.board.entity.Post;
import com.cpu.web.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PostRequestDTO {

    private Boolean isNotice;
    private String  title;
    private String content;

    public PostRequestDTO(){}

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