package com.cpu.web.scholarship.controller;

import com.cpu.web.scholarship.dto.StudyDTO;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
@Tag(name = "Study", description = "스터디 API")
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    @Operation(summary = "스터디 생성", description = "스터디 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "studyName", description = "스터디 이름", schema = @Schema(type = "string", example = "알고리즘 스터디")),
            @Parameter(name = "studyDescription", description = "스터디 설명", schema = @Schema(type = "string", example = "알고리즘을 공부하는 스터디입니다.")),
            @Parameter(name = "studyType", description = "스터디 타입", schema = @Schema(type = "string", example = "study")),
            @Parameter(name = "maxMembers", description = "최대 인원", schema = @Schema(type = "integer", example = "10")),
            @Parameter(name = "techStack", description = "기술 스택", schema = @Schema(type = "string", example = "Java, Spring, React")),
            @Parameter(name = "studyDays", description = "진행 요일 (월~일) 리스트", schema = @Schema(type = "array", example = "[\"Mon\", \"Wed\", \"Fri\"]")),            @Parameter(name = "location", description = "스터디 장소", schema = @Schema(type = "string", example = "서울 강남구")),
            @Parameter(name = "etc", description = "기타 정보", schema = @Schema(type = "string", example = "초보자 환영"))
    })
    public ResponseEntity<StudyDTO> createStudy(@RequestBody StudyDTO studyDTO) {
        Study study = studyService.createStudy(studyDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(study.getStudyId())
                .toUri();
        return ResponseEntity.created(location).body(new StudyDTO(study));
    }

    @GetMapping
    @Operation(summary = "스터디 전체 조회", description = "스터디 전체 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public Page<StudyDTO> getAllStudies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return studyService.getAllStudies(page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "스터디 개별 조회", description = "스터디 개별 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 스터디입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<StudyDTO> getStudyById(@PathVariable Long id) {
        Optional<StudyDTO> studyDTO = studyService.getStudyById(id);
        return studyDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "스터디 수정", description = "스터디 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "studyName", description = "스터디 이름", schema = @Schema(type = "string", example = "알고리즘 스터디")),
            @Parameter(name = "studyDescription", description = "스터디 설명", schema = @Schema(type = "string", example = "알고리즘을 공부하는 스터디입니다.")),
            @Parameter(name = "studyType", description = "스터디 타입", schema = @Schema(type = "string", example = "study")),
            @Parameter(name = "maxMembers", description = "최대 인원", schema = @Schema(type = "integer", example = "10")),
            @Parameter(name = "techStack", description = "기술 스택", schema = @Schema(type = "string", example = "Java, Spring, React")),
            @Parameter(name = "studyDays", description = "진행 요일 (월~일) 리스트", schema = @Schema(type = "array", example = "[\"Mon\", \"Wed\", \"Fri\"]")),
            @Parameter(name = "location", description = "스터디 장소", schema = @Schema(type = "string", example = "서울 강남구")),
            @Parameter(name = "etc", description = "기타 정보", schema = @Schema(type = "string", example = "초보자 환영"))
    })
    public ResponseEntity<StudyDTO> updateStudy(@PathVariable Long id, @RequestBody StudyDTO studyDTO) {
        StudyDTO updatedStudyDTO = studyService.updateStudy(id, studyDTO);
        return ResponseEntity.ok(updatedStudyDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "스터디 삭제", description = "스터디 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> deleteStudy(@PathVariable Long id) {
        studyService.deleteStudy(id);
        return ResponseEntity.noContent().build();
    }
}
