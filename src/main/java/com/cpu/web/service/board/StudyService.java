package com.cpu.web.service.board;

import com.cpu.web.dto.board.StudyDTO;
import com.cpu.web.entity.board.Study;
import com.cpu.web.repository.board.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public Study createStudy(StudyDTO studyDTO) {
        Study study = studyDTO.toStudyEntity();
        return studyRepository.save(study);
    }

    public List<StudyDTO> getAllStudies() {
        return studyRepository.findAll().stream()
                .map(StudyDTO::new)
                .collect(Collectors.toList());
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