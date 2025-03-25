package com.cpu.web.board.repository;

import com.cpu.web.board.entity.Gathering;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Gathering> findByGatheringId(Long gatheringId);
}
