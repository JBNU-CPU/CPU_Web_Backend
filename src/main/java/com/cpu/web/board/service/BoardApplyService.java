package com.cpu.web.board.service;

import com.cpu.web.board.entity.Gathering;
import com.cpu.web.board.entity.MemberGathering;
import com.cpu.web.board.repository.GatheringRepository;
import com.cpu.web.board.repository.MemberGatheringRepository;
import com.cpu.web.exception.CustomException;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardApplyService {

    private final MemberGatheringRepository memberGatheringRepository;
    private final GatheringRepository gatheringRepository;
    private final MemberRepository memberRepository;

    public void applyGathering(Long gatheringId) {

        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        Long memberId = member.getMemberId();

        // 스터디 존재 여부 확인
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new CustomException("존재하지 않는 소모임입니다. ID: " + gatheringId, HttpStatus.NOT_FOUND));

        // 이미 참여한 멤버인지 확인
        boolean isMember = memberGatheringRepository.existsByGathering_GatheringIdAndMember_MemberId(gatheringId, memberId);
        if(isMember) {
            throw new CustomException("이미 참여 중인 소모임입니다. ID: " + gatheringId, HttpStatus.BAD_REQUEST);
        }

        // 리더인지 확인
        if (gathering.getMember().equals(member)) {
            throw new CustomException("소모임 개설자는 신청할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 정원이 초과됏는지 확인
        Long currentCount = memberGatheringRepository.countByGathering(gathering);
        if (currentCount >= gathering.getMaxMembers()){
            throw new CustomException("이미 최대 인원이 찼습니다.", HttpStatus.BAD_REQUEST);
        }

        // 신청 정보 저장 (isLeader = false)

        MemberGathering memberGathering = new MemberGathering();
        memberGathering.setGathering(gathering);
        memberGathering.setMember(member);
        memberGathering.setIsLeader(false);

        memberGatheringRepository.save(memberGathering);

    }

    // 스터디 신청 취소
    public void cancelApply(Long gatheringId) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        Long memberId = member.getMemberId();

        // 참여 여부 확인 (참여 기록을 가져와야 하므로 findBy 사용)
        MemberGathering memberGathering = memberGatheringRepository
                .findByGathering_GatheringIdAndMember_MemberId(gatheringId, memberId)
                .orElseThrow(() -> new CustomException("소모임에 참여한 적이 없습니다.", HttpStatus.BAD_REQUEST));

        // 리더인지 확인 (리더는 신청 취소 불가)
        if (memberGathering.getIsLeader()) {
            throw new CustomException("소모임 개설자는 신청 취소할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 신청 취소 (삭제)
        memberGatheringRepository.delete(memberGathering);
    }
}
