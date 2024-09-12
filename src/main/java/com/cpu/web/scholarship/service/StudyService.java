package com.cpu.web.scholarship.service;

import com.cpu.web.scholarship.dto.StudyDTO;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public Study createStudy(StudyDTO studyDTO) {
        Study study = studyDTO.toStudyEntity();
        return studyRepository.save(study);
    }

    // 페이징 처리된 스터디 글 조회
    public Page<StudyDTO> getAllStudies(int page, int size) {
        Page<Study> studies = studyRepository.findAll(PageRequest.of(page, size));
        return studies.map(StudyDTO::new);
    }

    public Optional<StudyDTO> getStudyById(Long id) {
        return studyRepository.findById(id).map(StudyDTO::new);
    }

    public StudyDTO updateStudy(Long id, StudyDTO studyDTO) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid study ID: " + id));
        study.setTitle(studyDTO.getTitle());
        study.setContent(studyDTO.getContent());
        study.setIsAnonymous(studyDTO.isAnonymous());
        Study updatedStudy = studyRepository.save(study);
        return new StudyDTO(updatedStudy);
    }

    public void deleteStudy(Long id) {
        if (!studyRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid study ID: " + id);
        }
        studyRepository.deleteById(id);
    }
}