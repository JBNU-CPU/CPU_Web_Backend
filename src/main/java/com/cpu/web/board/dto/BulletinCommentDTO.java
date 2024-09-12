package com.cpu.web.board.dto;

import com.cpu.web.board.entity.Bulletin;
import com.cpu.web.board.entity.BulletinComment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulletinCommentDTO {

    private Long bulletinId;
    private String content;

    public BulletinCommentDTO(){}

    // entity => dto
    public BulletinCommentDTO(BulletinComment bulletinComment) {
        this.content = bulletinComment.getContents();
        this.bulletinId = bulletinComment.getBulletin().getBulletinId();
    }

    // dto => entity
    public BulletinComment toBulletinCommentEntity(String content, Bulletin bulletin){
        BulletinComment bulletinComment = new BulletinComment();

        bulletinComment.setContents(content);
        bulletinComment.setBulletin(bulletin);

        return bulletinComment;
    }
}