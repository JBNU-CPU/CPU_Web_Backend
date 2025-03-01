package com.cpu.web.scholarship.service;

import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import com.cpu.web.scholarship.dto.request.StudyRequestDTO;
import com.cpu.web.scholarship.dto.response.StudyResponseDTO;
import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.repository.MemberStudyRepository;
import com.cpu.web.scholarship.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final MemberStudyRepository memberStudyRepository;

    // 스터디 개설
    public Study createStudy(StudyRequestDTO studyRequestDTO) {

        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member leader = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 스터디 생성 및 저장
        Study study = studyRequestDTO.toStudyEntity(leader);
        Study savedStudy = studyRepository.save(study);

        // 매핑 테이블에 팀장 정보 추가
        MemberStudy memberStudy = new MemberStudy();
        memberStudy.setMember(leader);
        memberStudy.setStudy(savedStudy);
        memberStudy.setIsLeader(true);

        memberStudyRepository.save(memberStudy);

        return savedStudy;
    }

    // 스터디 전체 조회
    public Page<StudyResponseDTO> getAllStudies(int page, int size, String studyType) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("studyId").descending());
        if (studyType != null && !studyType.isEmpty()) {
            Study.StudyType type = Study.StudyType.valueOf(studyType.toLowerCase());
            return studyRepository.findByStudyType(type, pageRequest)
                    .map(study -> {
                        Long currentCount = memberStudyRepository.countByStudy(study); // ✅ 참여자 수 조회
                        return new StudyResponseDTO(study, currentCount); // ✅ DTO에 추가
                    });
        }
        return studyRepository.findAll(pageRequest)
                .map(study -> {
                    Long currentCount = memberStudyRepository.countByStudy(study); // ✅ 참여자 수 조회
                    return new StudyResponseDTO(study, currentCount); // ✅ DTO에 추가
                });
    }

    // 스터디 개별 조회
    public Optional<StudyResponseDTO> getStudyById(Long id) {
        Optional<Study> studyOpt = studyRepository.findById(id);
        if (studyOpt.isEmpty()) {
            return Optional.empty();
        }

        Study study = studyOpt.get();
        List<MemberStudy> memberStudies = memberStudyRepository.findByStudy_StudyId(id);
        Long currentCount = memberStudyRepository.countByStudy(study); // ✅ 참여자 수 조회

        return Optional.of(new StudyResponseDTO(study, memberStudies, currentCount)); // ✅ DTO에 추가
    }

    // 스터디 수정
    public StudyResponseDTO updateStudy(Long id, StudyRequestDTO studyRequestDTO) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Long leaderId = member.get().getMemberId(); // ✅ 현재 로그인한 사용자 ID 가져오기

        // 스터디 찾기
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid study ID: " + id));

        // 스터디 리더인지 확인
        if (!study.getLeaderId().equals(leaderId)) {
            throw new IllegalArgumentException("팀장이 아니므로 수정 권한이 없습니다: " + leaderId);
        }

        // 스터디 등록 시 수정 불가
        if(study.getIsAccepted()){
            throw new IllegalArgumentException("스터디가 등록되었으므로 수정할 수 없습니다.");
        }

        Study updatedStudy = studyRequestDTO.toStudyEntity(member.get());
        updatedStudy.setStudyId(study.getStudyId());
        // studyType 변환 처리
        String typeStr = studyRequestDTO.getStudyType().toLowerCase().trim();
        switch (typeStr) {
            case "study":
                updatedStudy.setStudyType(Study.StudyType.study);
                break;
            case "session":
                updatedStudy.setStudyType(Study.StudyType.session);
                break;
            case "project":
                updatedStudy.setStudyType(Study.StudyType.project);
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 스터디 타입입니다: " + studyRequestDTO.getStudyType());
        }

        return new StudyResponseDTO(studyRepository.save(updatedStudy));
    }

    // 스터디 삭제
    public void deleteStudy(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Long leaderId = member.get().getMemberId(); // ✅ 현재 로그인한 사용자 ID 가져오기
        System.out.println("스터디 삭제 username = " + username);
        // 스터디 찾기
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid study ID: " + id));

        // 관리자이거나 스터디 개설자인 경우 삭제 가능
        if (isAdmin || username.equals(study.getLeaderName())) {
            studyRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("삭제 권한이 없는 유저입니다: " + username);
        }

        studyRepository.deleteById(id);
    }

}
