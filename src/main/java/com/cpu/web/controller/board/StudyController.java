package com.cpu.web.controller.board;

import com.cpu.web.dto.board.StudyDTO;
import com.cpu.web.entity.board.Study;
import com.cpu.web.service.board.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<StudyDTO> createStudy(@RequestBody StudyDTO studyDTO) {
        Study study = studyService.createStudy(studyDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(study.getStudyId())
                .toUri();
        return ResponseEntity.created(location).body(studyDTO);
    }

    @GetMapping
    public List<StudyDTO> getAllStudies() {
        return studyService.getAllStudies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyDTO> getStudyById(@PathVariable Long id) {
        Optional<StudyDTO> studyDTO = studyService.getStudyById(id);
        return studyDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyDTO> updateStudy(@PathVariable Long id, @RequestBody StudyDTO studyDTO) {
        StudyDTO updatedStudyDTO = studyService.updateStudy(id, studyDTO);
        return ResponseEntity.ok(updatedStudyDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudy(@PathVariable Long id) {
        studyService.deleteStudy(id);
        return ResponseEntity.noContent().build();
    }
}