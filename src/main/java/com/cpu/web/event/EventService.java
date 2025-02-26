package com.cpu.web.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    // 점수가 높은 순으로 정렬하여 조회
    public List<EventResponseDTO> getAllScores() {
        List<Event> events = eventRepository.findAll(Sort.by(Sort.Direction.DESC, "score")); // score 기준 내림차순 정렬
        return events.stream()
                .map(EventResponseDTO::new)
                .collect(Collectors.toList());
    }
}
