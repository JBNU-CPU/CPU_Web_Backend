package com.cpu.web.scholarship.service;

import com.cpu.web.member.repository.MemberRepository;
import com.cpu.web.scholarship.dto.StudyDTO;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.entity.MemberStudy;
import com.cpu.web.scholarship.repository.StudyRepository;
import com.cpu.web.scholarship.repository.MemberStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final MemberStudyRepository memberStudyRepository;

    public Study createStudy(StudyDTO studyDTO, Long memberId) {
        Optional<MemberStudy> memberStudyOpt = memberStudyRepository.findByMember_MemberId(memberId).stream().findFirst();
        String name = studyDTO.getStudyName();
        String description = studyDTO.getStudyDescription();
        Enum type = studyDTO.getStudyType();
        int max = studyDTO.getMaxMembers();

        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("이름이 유효하지 않습니다.");
        }

        if (description == null || description.isEmpty() || description.isBlank()) {
            throw new IllegalArgumentException("설명이 유효하지 않습니다.");
        }

        if (type == null) {
            throw new IllegalArgumentException("타입이 유효하지 않습니다.");
        }

        if (max <= 0) {
            throw new IllegalArgumentException("최대 인원이 유효하지 않습니다.");
        }

        if (memberStudyOpt.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        MemberStudy memberStudy = memberStudyOpt.get();
        memberStudy.setIsLeader(true); // 작성자를 팀장으로 설정
        memberStudyRepository.save(memberStudy);

        Study study = studyDTO.toStudyEntity();
        study.setMemberId(memberId); // MemberId 설정
        return studyRepository.save(study);
    }

    public Page<StudyDTO> getAllStudies(int page, int size) {
        Page<Study> studies = studyRepository.findAll(PageRequest.of(page, size));
        return studies.map(StudyDTO::new);
    }

    public Optional<StudyDTO> getStudyById(Long id) {
        return studyRepository.findById(id).map(StudyDTO::new);
    }

    public StudyDTO updateStudy(Long id, StudyDTO studyDTO, Long memberId) {
        Optional<MemberStudy> memberStudyOpt = memberStudyRepository.findByStudy_StudyIdAndMember_MemberId(id, memberId);

        if (memberStudyOpt.isEmpty()) {
            throw new IllegalArgumentException("수정 권한이 없는 유저입니다: " + memberId);
        }

        MemberStudy memberStudy = memberStudyOpt.get();
        if (!memberStudy.getIsLeader()) {
            throw new IllegalArgumentException("팀장이 아니므로 수정 권한이 없습니다: " + memberId);
        }

        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid study ID: " + id));

        study.setStudyName(studyDTO.getStudyName());
        study.setStudyType(studyDTO.getStudyType());
        study.setStudyDescription(studyDTO.getStudyDescription());
        study.setMaxMembers(studyDTO.getMaxMembers());
        return new StudyDTO(studyRepository.save(study));
    }

    public void deleteStudy(Long id, Long memberId) {
        Optional<MemberStudy> memberStudyOpt = memberStudyRepository.findByStudy_StudyIdAndMember_MemberId(id, memberId);

        if (memberStudyOpt.isEmpty()) {
            throw new IllegalArgumentException("삭제 권한이 없는 유저입니다: " + memberId);
        }

        MemberStudy memberStudy = memberStudyOpt.get();
        if (!memberStudy.getIsLeader()) {
            throw new IllegalArgumentException("팀장이 아니므로 삭제 권한이 없습니다: " + memberId);
        }

        if (!studyRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid Study ID: " + id);
        }

        studyRepository.deleteById(id);
    }
}
