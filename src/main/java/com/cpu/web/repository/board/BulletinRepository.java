package com.cpu.web.repository.board;

import com.cpu.web.entity.board.Bulletin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Long> {
    Page<Bulletin> findAll(Pageable pageable); // 페이징 기능 추가
}
