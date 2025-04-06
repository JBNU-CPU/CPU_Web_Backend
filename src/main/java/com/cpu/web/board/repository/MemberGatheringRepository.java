package com.cpu.web.board.repository;

import com.cpu.web.board.entity.Gathering;
import com.cpu.web.board.entity.MemberGathering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberGatheringRepository extends JpaRepository<MemberGathering, Long> {

    // 특정 소모임에 속한 모든 멤버 조회
    List<MemberGathering> findByGathering_GatheringId(Long gatheringId);

    // 소모임 신청 인원 수 조회
    Long countByGathering(Gathering gathering);

    // 특정 멤버가 특정 소모임에 참여하고 있는지 확인
    boolean existsByGathering_GatheringIdAndMember_MemberId(Long gatheringId, Long memberId);

    // 특정 멤버의 신청 정보 조회
    Optional<MemberGathering> findByGathering_GatheringIdAndMember_MemberId(Long gatheringId, Long memberId);

    void deleteByGathering(Gathering gathering);
}
