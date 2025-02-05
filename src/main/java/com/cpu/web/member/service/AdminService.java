package com.cpu.web.member.service;

import com.cpu.web.member.dto.MemberDTO;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import com.cpu.web.scholarship.dto.StudyDTO;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    // 전체 유저 조회
    public List<MemberDTO> getAllUser() {
        return memberRepository.findAll().stream()
                .map(MemberDTO::new)
                .collect(Collectors.toList());
    }

    // 특정 유저 권한 수정
    public MemberDTO updateRole(Long id, String role) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다: " + id));

        // 문자열로 받은 권한을 Role enum으로 변환
        try {
            Member.Role newRole = Member.Role.valueOf(role);  // 문자열을 Role enum으로 변환
            member.setRole(newRole);  // Role 설정
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 권한입니다: " + role);
        }

        Member updatedMember = memberRepository.save(member);
        return new MemberDTO(updatedMember);
    }

    // 유저 삭제
    public void deleteUser(Long id) {
        if (!memberRepository.existsById(id)){
            throw new IllegalArgumentException("유저가 존재하지 않습니다.: " + id);
        }
        memberRepository.deleteById(id);
    }
    
    
    // 전체 스터디 조회
    public List<StudyDTO> getAllStudy() {
        return studyRepository.findAll().stream()
                .map(StudyDTO::new)
                .collect(Collectors.toList());
    }

    // 스터디 등록
    public StudyDTO acceptStudy(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다: " + id));
        study.setIsAccepted(true);
        Study updatedStudy = studyRepository.save(study);
        return new StudyDTO(updatedStudy);
    }

    // 스터디 등록 취소
    public StudyDTO unacceptStudy(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다: " + id));
        study.setIsAccepted(false);
        Study updatedStudy = studyRepository.save(study);
        return new StudyDTO(updatedStudy);
    }

    // 스터디 삭제

    public void deleteStudy(Long id) {
        if (!studyRepository.existsById(id)){
            throw new IllegalArgumentException("유저가 존재하지 않습니다.: " + id);
        }
        studyRepository.deleteById(id);
    }
}
