package com.cpu.web.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventResponseDTO {
    private String userId;
    private Integer score;
    private String nickName;

    public EventResponseDTO(){

    }
    public EventResponseDTO(Event event){
        userId = event.getUserId();
        score = event.getScore();
        nickName = event.getNickName();
    }
}
