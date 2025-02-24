package com.cpu.web.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    // 이벤트 점수 등록
    @PostMapping
    public void createPost(@RequestBody EventRequestDTO eventRequestDTO){
        eventService.createScore(eventRequestDTO);
        System.out.println("eventRequestDTO.getUserId() = " + eventRequestDTO.getUserId());
    }
}
