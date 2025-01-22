package com.cpu.web.scholarship.repository;

import com.cpu.web.scholarship.entity.MemberStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberStudyRepository extends JpaRepository<MemberStudy, Long> {
    // 특정 멤버가 참여한 모든 스터디를 찾는 메소드
    List<MemberStudy> findByMember_MemberId(Long memberId);

    // 특정 멤버가 특정 스터디에 참여하고 있는지 확인하는 메소드
    Optional<MemberStudy> findByStudy_StudyIdAndMember_MemberId(Long studyId, Long memberId);
}

