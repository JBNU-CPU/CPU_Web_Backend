package com.cpu.web.scholarship.repository;

import com.cpu.web.scholarship.entity.Study;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study,Long> {
    Page<Study> findAll(Pageable pageable);
    Page<Study> findByStudyType(Study.StudyType studyType, Pageable pageable); // 여기에 추가
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Study s WHERE s.studyId = :studyId")
    Optional<Study> findByIdWithLock(@Param("studyId") Long studyId);
}
