package com.cpu.web.board.dto;

import com.cpu.web.board.entity.Bulletin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulletinDTO {
    private String  title;
    private String content;

    public BulletinDTO(){}


    // entity => dto
    public BulletinDTO(Bulletin bulletin) {
        this.title = bulletin.getTitle();
        this.content = bulletin.getContent();
    }

    // dto => entity
    public Bulletin toBulletinEntity(){
        Bulletin bulletin = new Bulletin();
        bulletin.setTitle(title);
        bulletin.setContent(content);
        return bulletin;
    }


}