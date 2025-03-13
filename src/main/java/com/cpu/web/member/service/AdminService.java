package com.cpu.web.member.service;

import com.cpu.web.exception.CustomException;
import com.cpu.web.member.dto.response.MemberResponseDTO;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import com.cpu.web.scholarship.dto.response.StudyResponseDTO;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.cpu.web.member.entity.Member.Role;
import static com.cpu.web.member.entity.Member.Role.*;
import static com.cpu.web.scholarship.entity.Study.StudyType;
import static com.cpu.web.scholarship.entity.Study.StudyType.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    // 전체 유저 조회
    public Page<MemberResponseDTO> getAllUser(int page, int size) {
        Page<Member> members = memberRepository.findAll(PageRequest.of(page, size));
        return members.map(MemberResponseDTO::new);
    }

    // 특정 권한 유저 전체 조회
    public Page<MemberResponseDTO> getUsersByRole(String role, int page, int size) {

        // 문자열로 받은 권한을 Role enum으로 변환
        Role enumRole = switch (role) {
            case "admin" -> ROLE_ADMIN;
            case "guest" -> ROLE_GUEST;
            case "member" -> ROLE_MEMBER;
            default -> throw new CustomException("유효하지 않은 권한입니다.", HttpStatus.BAD_REQUEST);
        };
        Page<Member> members =  memberRepository.findByRole(enumRole, PageRequest.of(page, size));
        return members.map(MemberResponseDTO::new);
    };
    
    // 특정 유저 권한 수정
    public MemberResponseDTO updateRole(Long id, String role) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.UNAUTHORIZED));

        // 문자열로 받은 권한을 Role enum으로 변환
        Role enumRole = switch (role) {
            case "admin" -> ROLE_ADMIN;
            case "guest" -> ROLE_GUEST;
            case "member" -> ROLE_MEMBER;
            default -> throw new CustomException("유효하지 않은 권한입니다.", HttpStatus.FORBIDDEN);
        };
        member.setRole(enumRole);
        Member updatedMember = memberRepository.save(member);
        return new MemberResponseDTO(updatedMember);
    }

    // 유저 삭제
    public void deleteUser(Long id) {
        if (!memberRepository.existsById(id)){
            throw new CustomException("유저가 존재하지 않습니다.: " + id, HttpStatus.NOT_FOUND);
        }
        memberRepository.deleteById(id);
    }

    // 전체 스터디 조회
    public Page<StudyResponseDTO> getAllStudy(int page, int size) {
        Page<Study> studies = studyRepository.findAll(PageRequest.of(page, size));
        return studies.map(StudyResponseDTO::new);
    }

    // 특정 타입 스터디 전체 조회
    public Page<StudyResponseDTO> getStudiesByStudyType(String studyType, int page, int size) {

        // 문자열로 받은 권한을 Role enum으로 변환
        StudyType enumStudyType = switch (studyType) {
            case "session" -> session;
            case "study" -> study;
            case "project" -> project;
            default -> throw new CustomException("유효하지 않은 스터디 타입입니다.", HttpStatus.BAD_REQUEST);
        };
        Page<Study> studies =  studyRepository.findByStudyType(enumStudyType, PageRequest.of(page, size));
        return studies.map(StudyResponseDTO::new);
    };

    // 스터디 등록
    public StudyResponseDTO acceptStudy(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new CustomException("스터디가 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));
        study.setIsAccepted(true);
        Study updatedStudy = studyRepository.save(study);
        return new StudyResponseDTO(updatedStudy);
    }

    // 스터디 등록 취소
    public StudyResponseDTO unacceptStudy(Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new CustomException("스터디가 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));
        study.setIsAccepted(false);
        Study updatedStudy = studyRepository.save(study);
        return new StudyResponseDTO(updatedStudy);
    }

}
