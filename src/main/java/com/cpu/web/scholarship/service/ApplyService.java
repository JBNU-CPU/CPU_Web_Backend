package com.cpu.web.scholarship.service;

import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.repository.MemberStudyRepository;
import com.cpu.web.scholarship.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final MemberStudyRepository memberStudyRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    // ✅ 스터디 신청
    public void applyStudy(Long studyId) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("현재 로그인한 사용자 (스터디 신청)" + username);
        Optional<Member> member = memberRepository.findByUsername(username);
        
        
        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Long memberId = member.get().getMemberId();

        // 스터디 존재 여부 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. ID: " + studyId));

        // 이미 참여한 멤버인지 확인
        boolean isMember = memberStudyRepository.existsByStudy_StudyIdAndMember_MemberId(studyId, memberId);
        if (isMember) {
            throw new IllegalArgumentException("이미 참여 중인 스터디입니다.");
        }

        // 리더인지 확인
        boolean isLeader = memberStudyRepository.existsByStudy_StudyIdAndMember_MemberIdAndIsLeader(studyId, memberId, true);
        if (isLeader) {
            throw new IllegalArgumentException("스터디 리더는 신청할 수 없습니다.");
        }
        
        // 정원이 초과됏는지 확인
        long currentCount = memberStudyRepository.countByStudy(study);
        if (currentCount >= study.getMaxMembers()){
            throw new RuntimeException("이미 최대 인원이 찼습니다.");
        }

        // 신청 정보 저장 (isLeader = false)
        MemberStudy memberStudy = new MemberStudy();
        memberStudy.setStudy(study);
        memberStudy.setMember(member.get());
        memberStudy.setIsLeader(false);

        memberStudyRepository.save(memberStudy);
    }

    // ✅ 스터디 신청 취소
    @Transactional
    public void cancelApply(Long studyId) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Long memberId = member.get().getMemberId();

        // 참여 여부 확인
        Optional<MemberStudy> memberStudy = memberStudyRepository.findByStudy_StudyIdAndMember_MemberId(studyId, memberId);
        if (memberStudy.isEmpty()) {
            throw new IllegalArgumentException("스터디에 참여한 적이 없습니다.");
        }

        // 리더인지 확인 (리더는 신청 취소 불가)
        if (memberStudy.get().getIsLeader()) {
            throw new IllegalArgumentException("스터디 리더는 신청 취소할 수 없습니다.");
        }

        // 신청 취소 (삭제)
        memberStudyRepository.delete(memberStudy.get());
    }
}
