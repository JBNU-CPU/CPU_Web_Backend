package com.cpu.web.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    private String eventCode = "1234";

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
    
    // 이벤트 코드 등록
    @PostMapping("/eventcode")
    public String updateEventCode(@RequestParam String eventCode){
        this.eventCode = eventCode;
        return "Updated!";
    }

    // 이벤트 코드 조회
    @GetMapping("/eventcode")
    public String getEventCode(){
        return this.eventCode;
    }
}
