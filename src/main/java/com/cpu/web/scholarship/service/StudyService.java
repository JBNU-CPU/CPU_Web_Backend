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

import java.util.ArrayList;
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

        Long leaderId = member.get().getMemberId(); // ✅ 리더 ID 가져오기

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
        study.setLeaderId(leaderId); // ✅ 리더 ID 설정
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


    public Page<StudyDTO> getAllStudies(int page, int size, String studyType) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (studyType != null && !studyType.isEmpty()) {
            Study.StudyType type = Study.StudyType.valueOf(studyType.toLowerCase());
            return studyRepository.findByStudyType(type, pageRequest).map(StudyDTO::new);
        }
        return studyRepository.findAll(pageRequest).map(StudyDTO::new);
    }

    public Optional<StudyDTO> getStudyById(Long id) {
        // ✅ 스터디 정보 가져오기
        Optional<Study> study = studyRepository.findById(id);
        if (study.isEmpty()) {
            return Optional.empty();
        }

        // ✅ 해당 스터디에 참여 중인 멤버 정보 가져오기
        List<MemberStudy> memberStudies = memberStudyRepository.findByStudy_StudyId(id);

        // ✅ StudyDTO 변환
        return Optional.of(new StudyDTO(study.get(), memberStudies));
    }


    public StudyDTO updateStudy(Long id, StudyDTO studyDTO) {
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

        // ✅ 스터디 리더인지 확인
        if (!study.getLeaderId().equals(leaderId)) {
            throw new IllegalArgumentException("팀장이 아니므로 수정 권한이 없습니다: " + leaderId);
        }

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

        // 진행 요일 변환
        List<String> studyDayStrs = studyDTO.getStudyDays();
        List<Study.StudyDay> studyDays;
        try {
            studyDays = studyDayStrs.stream()
                    .map(day -> Study.StudyDay.valueOf(day.toUpperCase()))
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 진행 요일입니다: " + studyDayStrs);
        }
        study.setStudyDays(studyDays);

        return new StudyDTO(studyRepository.save(study));
    }


    public void deleteStudy(Long id) {
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

        // ✅ 스터디 리더인지 확인
        if (!study.getLeaderId().equals(leaderId)) {
            throw new IllegalArgumentException("팀장이 아니므로 삭제 권한이 없습니다: " + leaderId);
        }

        studyRepository.deleteById(id);
    }

}
