package com.cpu.web.scholarship.service;

import com.cpu.web.exception.CustomException;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import com.cpu.web.scholarship.dto.request.StudyRequestDTO;
import com.cpu.web.scholarship.dto.response.StudyResponseDTO;
import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.repository.MemberStudyRepository;
import com.cpu.web.scholarship.repository.StudyRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final MemberStudyRepository memberStudyRepository;

    // 스터디 개설
    public Study createStudy(@Valid StudyRequestDTO studyRequestDTO) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member leader = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        // 스터디 생성 및 저장
        Study study = studyRequestDTO.toStudyEntity(leader);
        study.setIsClosed(false); // 초기에는 항상 false로 설정

        // 스터디에 대한 매핑 테이블에 팀장 정보 추가
        MemberStudy memberStudy = new MemberStudy();
        memberStudy.setMember(leader);
        memberStudy.setStudy(study);
        memberStudy.setIsLeader(true);

        // 스터디 저장
        Study savedStudy = studyRepository.save(study);
        memberStudyRepository.save(memberStudy);

        // 스터디 마감 여부 검사 및 설정
        updateStudyClosureStatus(savedStudy);

        return savedStudy;
    }

    // 스터디 전체 조회
    public Page<StudyResponseDTO> getAllStudies(int page, int size, String studyType) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("studyId").descending());
        if (studyType != null && !studyType.isEmpty()) {
            Study.StudyType type = Study.StudyType.valueOf(studyType.toLowerCase());
            return studyRepository.findByStudyType(type, pageRequest)
                    .map(study -> {
                        Long currentCount = memberStudyRepository.countByStudy(study);
                        return new StudyResponseDTO(study, currentCount);
                    });
        }
        return studyRepository.findAll(pageRequest)
                .map(study -> {
                    Long currentCount = memberStudyRepository.countByStudy(study);
                    return new StudyResponseDTO(study, currentCount);
                });
    }

    // 스터디 개별 조회
    public Optional<StudyResponseDTO> getStudyById(Long id) {
        Study study = studyRepository.findById(id).orElseThrow(() -> new CustomException("스터디가 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));

        List<MemberStudy> memberStudies = memberStudyRepository.findByStudy_StudyId(id);
        Long currentCount = memberStudyRepository.countByStudy(study);

        return Optional.of(new StudyResponseDTO(study, memberStudies, currentCount));
    }

    // 스터디 수정
    public StudyResponseDTO updateStudy(Long id, @Valid StudyRequestDTO studyRequestDTO) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        // 스터디 찾기
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new CustomException("스터디가 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));

        // 현재 참여 인원 확인 및 관리자가 아닌 경우 검사
        Long currentCount = memberStudyRepository.countByStudy(study);
        if (!isAdmin && currentCount >= 2) {
            throw new CustomException("스터디 참여 인원이 2명 이상이므로 수정할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 스터디 리더인지 확인 및 관리자가 아닌 경우 검사
        if (!isAdmin && !study.getLeader().equals(member)) {
            throw new CustomException("팀장이 아니므로 수정 권한이 없습니다: " + member.getPersonName(), HttpStatus.FORBIDDEN);
        }

        studyRequestDTO.updateStudyEntity(study);  // 먼저 스터디 정보 업데이트

        // studyType 변환 처리
        String typeStr = studyRequestDTO.getStudyType().toLowerCase().trim();
        switch (typeStr) {
            case "study":
                study.setStudyType(Study.StudyType.study);
                break;
            case "session":
                study.setStudyType(Study.StudyType.session);
                break;
            case "project":
                study.setStudyType(Study.StudyType.project);
                break;
            default:
                throw new CustomException("유효하지 않은 스터디 타입입니다: " + studyRequestDTO.getStudyType(), HttpStatus.BAD_REQUEST);
        }

        // 스터디 상태 업데이트 (모든 변경 사항 후에 호출)
        updateStudyClosureStatus(study);

        return new StudyResponseDTO(studyRepository.save(study));
    }

    // 스터디 삭제
    public void deleteStudy(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.UNAUTHORIZED));

        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new CustomException("스터디가 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));

        // 현재 참여 인원 확인 및 관리자가 아닌 경우 검사
        Long currentCount = memberStudyRepository.countByStudy(study);
        if (!isAdmin && currentCount >= 2) {
            throw new CustomException("스터디 참여 인원이 2명 이상이므로 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 관리자이거나 스터디 개설자인 경우 삭제 가능
        if (!(isAdmin || member.equals(study.getLeader()))) {
            throw new CustomException("삭제 권한이 없는 유저입니다.", HttpStatus.FORBIDDEN);
        }

        studyRepository.delete(study);
    }

    // 스터디 마감 여부 업데이트 로직
    private void updateStudyClosureStatus(Study study) {
        long currentCount = memberStudyRepository.countByStudy(study);
        boolean isFull = currentCount == study.getMaxMembers();
        study.setIsClosed(isFull);
        studyRepository.save(study);
    }

    // 스터디 마감하기 (기존 API 유지, 토글 기능 추가)
    public StudyResponseDTO closeStudy(Long id) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        // 스터디 찾기
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new CustomException("스터디가 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));

        // 스터디 리더인지 확인 또는 관리자 권한 검사
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !study.getLeader().equals(member)) {
            throw new CustomException("팀장이 아니므로 마감 권한이 없습니다: " + member.getPersonName(), HttpStatus.FORBIDDEN);
        }

        // 현재 상태를 반대로 토글 (true ↔ false)
        study.setIsClosed(!study.getIsClosed());
        studyRepository.save(study);

        // 변경된 스터디 정보로 응답 객체 생성
        return new StudyResponseDTO(study);
    }
}
