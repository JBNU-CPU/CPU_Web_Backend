package com.cpu.web.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    public Event createScore(EventRequestDTO eventRequestDTO) {
        return eventRepository.save(eventRequestDTO.toEventEntity());
    }
}
