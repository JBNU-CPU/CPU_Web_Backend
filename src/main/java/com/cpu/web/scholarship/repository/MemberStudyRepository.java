package com.cpu.web.scholarship.repository;

import com.cpu.web.member.entity.Member;
import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberStudyRepository extends JpaRepository<MemberStudy, Long> {
    // 특정 멤버가 특정 스터디에 참여하고 있는지 확인
    boolean existsByStudy_StudyIdAndMember_MemberId(Long studyId, Long memberId);

    // 특정 멤버가 리더인지 확인
    boolean existsByStudy_StudyIdAndMember_MemberIdAndIsLeader(Long studyId, Long memberId, boolean isLeader);

    // 특정 멤버의 신청 정보 조회
    Optional<MemberStudy> findByStudy_StudyIdAndMember_MemberId(Long studyId, Long memberId);

    // 특정 스터디에 속한 모든 멤버 조회
    List<MemberStudy> findByStudy_StudyId(Long studyId);
    
    // 스터디 신청 인원 수 조회
    long countByStudy(Study study);

    // 특정 멤버가 팀원으로 참여하고 있는 스터디 전체 조회
    List<MemberStudy> findByMemberAndIsLeaderFalse(Member member);

    // 특정 멤버가 팀장으로 참여하고 있는 스터디 전체 조회
    List<MemberStudy> findByMemberAndIsLeaderTrue(Member member);

    // 특정 멤버가 참여하고 있는 모든 스터디 조회
    List<MemberStudy> findByMember_MemberId(Long memberId);


}
