package com.cpu.web.member.service;

import com.cpu.web.member.dto.MemberDTO;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import com.cpu.web.scholarship.dto.response.StudyResponseDTO;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.cpu.web.member.entity.Member.Role;
import static com.cpu.web.member.entity.Member.Role.*;

@Service
@RequiredArgsConstructor
public class AdminService {
//    관리자로 로그인 -> 매니지먼트 -> 유저관리 -> 승인
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    // 전체 유저 조회
    public List<MemberDTO> getAllUser() {
        return memberRepository.findAll().stream()
                .map(MemberDTO::new)
                .collect(Collectors.toList());
    }

    // 특정 권한 유저 전체 조회
    public List<MemberDTO> getUsersByRole(String role) {
        Role enumRole;
        if (role.equals("admin")){
            enumRole = ROLE_ADMIN;
        }else if (role.equals("guest")){
            enumRole = ROLE_GUEST;
        }else if (role.equals("member")){
            enumRole = ROLE_MEMBER;
        }else{
            throw new IllegalArgumentException("유효하지 않은 권한입니다.");
        }
        return memberRepository.findByRole(enumRole).stream()
                .map(MemberDTO::new)
                .collect(Collectors.toList());
    };
    
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
    public List<StudyResponseDTO> getAllStudy() {
        return studyRepository.findAll().stream()
                .map(StudyResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 스터디 등록
    public StudyResponseDTO acceptStudy(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다: " + id));
        study.setIsAccepted(true);
        Study updatedStudy = studyRepository.save(study);
        return new StudyResponseDTO(updatedStudy);
    }

    // 스터디 등록 취소
    public StudyResponseDTO unacceptStudy(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다: " + id));
        study.setIsAccepted(false);
        Study updatedStudy = studyRepository.save(study);
        return new StudyResponseDTO(updatedStudy);
    }

    // 스터디 삭제

    public void deleteStudy(Long id) {
        if (!studyRepository.existsById(id)){
            throw new IllegalArgumentException("유저가 존재하지 않습니다.: " + id);
        }
        studyRepository.deleteById(id);
    }

}
