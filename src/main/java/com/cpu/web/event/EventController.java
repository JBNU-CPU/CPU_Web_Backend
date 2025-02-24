package com.cpu.web.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    // 이벤트 점수 등록
    @PostMapping
    public void createScore(@RequestBody EventRequestDTO eventRequestDTO){
        eventService.createScore(eventRequestDTO);
    }
    
    // 이벤트 점수 전체 조회
    @GetMapping
    public List<EventResponseDTO> getScores(){
        return eventService.getAllScores();
    }
}
