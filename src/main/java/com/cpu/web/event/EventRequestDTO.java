package com.cpu.web.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRequestDTO {
    private String userId;
    private Integer score;
    private String nickName;

    public Event toEventEntity(){
        Event event = new Event();
        event.setUserId(userId);
        event.setScore(score);
        event.setNickName(nickName);
        return event;
    }
}
