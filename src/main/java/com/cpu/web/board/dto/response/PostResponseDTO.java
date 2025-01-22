package com.cpu.web.board.dto.response;

import com.cpu.web.board.entity.Post;
import com.cpu.web.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PostResponseDTO {

    private Long id;
    private Boolean isNotice;
    private String  title;
    private String content;
    private Timestamp createDate;
    private String nickName;

    public PostResponseDTO(){}


    // entity => dto
    public PostResponseDTO(Post post) {

        this.id = post.getPostId();
        this.isNotice = post.getIsNotice();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createDate = post.getCreateDate();
        this.nickName = post.getMember().getNickName();
    }
}