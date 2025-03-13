package com.cpu.web.board.repository;

import com.cpu.web.board.entity.Gathering;
import com.cpu.web.board.entity.MemberGathering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberGatheringRepository extends JpaRepository<MemberGathering, Long> {

    // 특정 소모임에 속한 모든 멤버 조회
    List<MemberGathering> findByGathering_GatheringId(Long gatheringId);

    // 소모임 신청 인원 수 조회
    Long countByGathering(Gathering gathering);
}
