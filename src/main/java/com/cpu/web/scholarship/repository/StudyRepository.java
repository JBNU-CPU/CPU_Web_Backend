package com.cpu.web.scholarship.repository;

import com.cpu.web.scholarship.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study,Long> {
    Page<Study> findAll(Pageable pageable);
    Page<Study> findByStudyType(Study.StudyType studyType, Pageable pageable); // 여기에 추가

}
