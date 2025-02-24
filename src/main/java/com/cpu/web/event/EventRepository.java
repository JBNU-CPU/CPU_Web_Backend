package com.cpu.web.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByUserId(String userId);

    Event findByUserId(String userId);
}
