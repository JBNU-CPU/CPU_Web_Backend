package com.cpu.web.repository.board;

import com.cpu.web.entity.board.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    // 페이징 처리된 스터디 글 목록 조회
    Page<Study> findAll(Pageable pageable);
}
