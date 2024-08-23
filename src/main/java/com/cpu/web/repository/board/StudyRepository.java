package com.cpu.web.repository.board;

import com.cpu.web.entity.board.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Page<Study> findAll(Pageable pageable); // 페이징 기능 추가
}
