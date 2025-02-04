package com.cpu.web.scholarship.service;

import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import com.cpu.web.scholarship.dto.StudyDTO;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.repository.StudyRepository;
import com.cpu.web.scholarship.repository.MemberStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Study createStudy(StudyDTO studyDTO) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        // DTO 값 검증
        String name = studyDTO.getStudyName();
        String description = studyDTO.getStudyDescription();
        String typeStr = studyDTO.getStudyType().toLowerCase().trim();
        int max = studyDTO.getMaxMembers();
        String techStack = studyDTO.getTechStack();
        List<String> studyDayStrs = studyDTO.getStudyDays();
        String location = studyDTO.getLocation();
        String etc = studyDTO.getEtc();

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 유효하지 않습니다.");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("설명이 유효하지 않습니다.");
        }

        if (typeStr == null || typeStr.isEmpty()) {
            throw new IllegalArgumentException("타입이 유효하지 않습니다.");
        }

        if (techStack == null || techStack.isBlank()) {
            throw new IllegalArgumentException("기술 스택이 유효하지 않습니다.");
        }

        if (studyDayStrs == null || studyDayStrs.isEmpty()) {
            throw new IllegalArgumentException("진행 요일이 유효하지 않습니다.");
        }

        if (max <= 0) {
            throw new IllegalArgumentException("최대 인원이 유효하지 않습니다.");
        }

        // 스터디 타입 변환
        Study.StudyType type;
        switch (typeStr) {
            case "study":
                type = Study.StudyType.study;
                break;
            case "session":
                type = Study.StudyType.session;
                break;
            case "project":
                type = Study.StudyType.project;
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 스터디 타입입니다: " + typeStr);
        }

        // 진행 요일 변환 (String 리스트 → ENUM 리스트)
        List<Study.StudyDay> studyDays;
        try {
            studyDays = studyDayStrs.stream()
                    .map(day -> Study.StudyDay.valueOf(day.toUpperCase()))
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 진행 요일입니다: " + studyDayStrs);
        }

        // 스터디 생성
        Study study = new Study();
        study.setMemberId(member.get().getMemberId());
        study.setStudyName(name);
        study.setStudyType(type);
        study.setMaxMembers(max);
        study.setStudyDescription(description);
        study.setTechStack(techStack);
        study.setStudyDays(studyDays);
        study.setLocation(location);
        study.setEtc(etc);

        Study savedStudy = studyRepository.save(study);

        // 팀장 정보 추가
        MemberStudy memberStudy = new MemberStudy();
        memberStudy.setMember(member.get());
        memberStudy.setStudy(savedStudy);
        memberStudy.setIsLeader(true);
        memberStudyRepository.save(memberStudy);

        return savedStudy;
    }

    public Page<StudyDTO> getAllStudies(int page, int size) {
        Page<Study> studies = studyRepository.findAll(PageRequest.of(page, size));
        return studies.map(StudyDTO::new);
    }

    public Optional<StudyDTO> getStudyById(Long id) {
        return studyRepository.findById(id).map(StudyDTO::new);
    }

    public StudyDTO updateStudy(Long id, StudyDTO studyDTO) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Long memberId = member.get().getMemberId();

        // 스터디 리더인지 검증
        Optional<MemberStudy> memberStudyOpt = memberStudyRepository.findByStudy_StudyIdAndMember_MemberId(id, memberId);

        if (memberStudyOpt.isEmpty() || !memberStudyOpt.get().getIsLeader()) {
            throw new IllegalArgumentException("팀장이 아니므로 수정 권한이 없습니다: " + memberId);
        }

        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid study ID: " + id));

        study.setStudyName(studyDTO.getStudyName());
        study.setStudyDescription(studyDTO.getStudyDescription());
        study.setMaxMembers(studyDTO.getMaxMembers());
        study.setTechStack(studyDTO.getTechStack());
        study.setLocation(studyDTO.getLocation());
        study.setEtc(studyDTO.getEtc());

        // studyType 변환 처리
        String typeStr = studyDTO.getStudyType().toLowerCase().trim();
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
                throw new IllegalArgumentException("유효하지 않은 스터디 타입입니다: " + studyDTO.getStudyType());
        }

        // 진행 요일 변환 (String 리스트 → ENUM 리스트)
        List<String> studyDayStrs = studyDTO.getStudyDays();
        List<Study.StudyDay> studyDays;
        try {
            studyDays = studyDayStrs.stream()
                    .map(day -> Study.StudyDay.valueOf(day.toUpperCase()))
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 진행 요일입니다: " + studyDayStrs);
        }
        study.setStudyDays(studyDays); // ✅ 수정 시에도 복수 요일 업데이트 가능

        return new StudyDTO(studyRepository.save(study));
    }

    public void deleteStudy(Long id) {
        // 로그인된 사용자 정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Long memberId = member.get().getMemberId();

        // 스터디 리더인지 검증
        Optional<MemberStudy> memberStudyOpt = memberStudyRepository.findByStudy_StudyIdAndMember_MemberId(id, memberId);

        if (memberStudyOpt.isEmpty() || !memberStudyOpt.get().getIsLeader()) {
            throw new IllegalArgumentException("팀장이 아니므로 삭제 권한이 없습니다: " + memberId);
        }

        if (!studyRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid Study ID: " + id);
        }

        studyRepository.deleteById(id);
    }
}
