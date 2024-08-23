package com.cpu.web.service.board;

import com.cpu.web.dto.board.StudyDTO;
import com.cpu.web.entity.board.Study;
import com.cpu.web.repository.board.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    // 페이징된 전체 글 조회
    public Page<StudyDTO> getAllStudies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studyRepository.findAll(pageable).map(this::convertToDTO);
    }

    // 특정 글 조회
    public StudyDTO getStudyById(Long id) {
        Optional<Study> study = studyRepository.findById(id);
        return study.map(this::convertToDTO).orElse(null);
    }

    // 글 저장
    public StudyDTO createStudy(StudyDTO studyDTO) {
        Study study = studyDTO.toStudyEntity();
        Study savedStudy = studyRepository.save(study);
        return convertToDTO(savedStudy);
    }

    // 글 수정
    public StudyDTO updateStudy(Long id, StudyDTO studyDTO) {
        Optional<Study> existingStudy = studyRepository.findById(id);
        if (existingStudy.isPresent()) {
            Study study = existingStudy.get();
            study.setTitle(studyDTO.getTitle());
            study.setContent(studyDTO.getContent());
            study.setIsAnonymous(studyDTO.isAnonymous());

            Study updatedStudy = studyRepository.save(study);
            return convertToDTO(updatedStudy);
        } else {
            return null; // 또는 적절한 예외 처리
        }
    }

    // 글 삭제
    public void deleteStudy(Long id) {
        studyRepository.deleteById(id);
    }

    private StudyDTO convertToDTO(Study study) {
        return new StudyDTO(study);
    }
}
