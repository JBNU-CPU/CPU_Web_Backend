package com.cpu.web.dto;

import com.cpu.web.entity.Bulletin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulletinDTO {
    private String  title;
    private String content;
    private boolean isAnonymous;

    public Bulletin toContentEntity(){

        Bulletin bulletin = new Bulletin();

        bulletin.setTitle(title);

        bulletin.setContent(content);

        bulletin.setAnonymous(isAnonymous);

        return bulletin;
    }
}

