package com.cpu.web.scholarship.service;

import com.cpu.web.scholarship.dto.StudyDTO;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public Study createStudy(StudyDTO studyDTO) {

        Long memberId=studyDTO.getMemberId();
        String studyName=studyDTO.getStudyName();
        Study.StudyType studyType=studyDTO.getStudyType();
        int maxMembers=studyDTO.getMaxMembers();
        String studyDescription=studyDTO.getStudyDescription();

        // 멤버아이디 유효한지
        if (memberId == null) {
            throw new IllegalArgumentException("맴버아이디 유효하지 않습니다.");
        }

        // studyName 유효한지
        if (studyName == null) {
            throw new IllegalArgumentException("studyName 유효하지 않습니다.");
        } else if (studyName.isEmpty()) {
            throw new IllegalArgumentException("studyName 유효하지 않습니다.");
        } else if (studyName.isBlank()) {
            throw new IllegalArgumentException("studyName 유효하지 않습니다.");
        }
        // studyType 유효한지
        if (studyType == null) {
            throw new IllegalArgumentException("studyType 유효하지 않습니다.");
        }
        // maxMembers 유효한지
        if (maxMembers == 0) {
            throw new IllegalArgumentException("maxMembers 유효하지 않습니다.");
        }
        // studyDescription 유효한지
        if (studyDescription == null) {
            throw new IllegalArgumentException("studyDescription 유효하지 않습니다.");
        } else if (studyDescription.isEmpty()) {
            throw new IllegalArgumentException("studyDescription 유효하지 않습니다.");
        } else if (studyDescription.isBlank()) {
            throw new IllegalArgumentException("studyDescription 유효하지 않습니다.");
        }

        Study study = studyDTO.toStudyEntity();
        return studyRepository.save(study);
    }

    // 페이징 처리된 스터디 글 조회
    /*public Page<StudyDTO> getAllStudies(int page, int size) {
        Page<Study> studies = studyRepository.findAll(PageRequest.of(page, size));
        return studies.map(StudyDTO::new);
    }

    public Optional<StudyDTO> getStudyById(Long id) {
        return studyRepository.findById(id).map(StudyDTO::new);
    }*/

    /*public StudyDTO updateStudy(Long id, StudyDTO studyDTO) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid study ID: " + id));
        study.setTitle(studyDTO.getTitle());
        study.setContent(studyDTO.getContent());
        study.setIsAnonymous(studyDTO.isAnonymous());
        Study updatedStudy = studyRepository.save(study);
        return new StudyDTO(updatedStudy);
    }*/

    /*public void deleteStudy(Long id) {
        if (!studyRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid study ID: " + id);
        }
        studyRepository.deleteById(id);
    }*/
}