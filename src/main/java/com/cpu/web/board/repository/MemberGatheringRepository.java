package com.cpu.web.board.repository;

import com.cpu.web.board.entity.MemberGathering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGatheringRepository extends JpaRepository<MemberGathering, Long> {

    // 특정 소모임에 속한 모든 멤버 조회
}
