package com.cpu.web.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public Event createScore(EventRequestDTO eventRequestDTO) {

        Event event = eventRepository.findByUserId(eventRequestDTO.getUserId());

        if (event != null) { // 기존 데이터가 있는 경우
            if (eventRequestDTO.getScore() > event.getScore()) { // 새로운 점수가 더 높다면 업데이트
                event.setScore(eventRequestDTO.getScore());
                return eventRepository.save(event);
            } else {
                return event; // 기존 값을 유지
            }
        } else { // 새로운 데이터 삽입
            return eventRepository.save(eventRequestDTO.toEventEntity());
        }
    }

    public void getScores() {
    }

    public List<EventResponseDTO> getAllScores() {
        List<Event> scores = eventRepository.findAll();
        return (List<EventResponseDTO>) scores.stream().map(EventResponseDTO::new);
    }
}
