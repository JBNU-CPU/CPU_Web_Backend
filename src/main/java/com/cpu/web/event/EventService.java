package com.cpu.web.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    
    public Event createScore(EventRequestDTO eventRequestDTO) {

        if (eventRepository.existsByUserId(eventRequestDTO.getUserId())){ // 이미 등록한 유저 점수 더 높을 경우 갱신
            Event event = eventRepository.findByUserId(eventRequestDTO.getUserId());
            if(eventRequestDTO.getScore() > event.getScore()){
                event.setScore(eventRequestDTO.getScore());
                return eventRepository.save(eventRequestDTO.toEventEntity());
            }else{
                return event;
            }
        }else{ // 신규 등록
            return eventRepository.save(eventRequestDTO.toEventEntity());
        }
    }
}
