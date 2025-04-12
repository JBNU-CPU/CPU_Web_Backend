package com.cpu.web.board.repository;

import com.cpu.web.board.entity.Gathering;
import com.cpu.web.scholarship.entity.Study;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Gathering> findByGatheringId(Long gatheringId);

    Page<Gathering> findAll(Pageable pageable);
}
