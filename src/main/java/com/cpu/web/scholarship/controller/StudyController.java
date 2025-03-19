package com.cpu.web.scholarship.controller;

import com.cpu.web.exception.CustomException;
import com.cpu.web.scholarship.dto.request.StudyRequestDTO;
import com.cpu.web.scholarship.dto.response.StudyResponseDTO;
import com.cpu.web.scholarship.entity.Study;
import com.cpu.web.scholarship.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyResponseDTO.class)))
    })
    public ResponseEntity<StudyResponseDTO> createStudy(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "스터디 개설 데이터",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyRequestDTO.class))
            ) @RequestBody @Valid StudyRequestDTO studyRequestDTO) {
        Study study = studyService.createStudy(studyRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(study.getStudyId())
                .toUri();
        return ResponseEntity.created(location).body(new StudyResponseDTO(study));
    }

    @GetMapping
    @Operation(summary = "스터디 전체 조회", description = "페이지네이션된 스터디 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyResponseDTO.class)))
    })
    public Page<StudyResponseDTO> getAllStudies(
            @Parameter(description = "페이지 번호 (0 이상)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기 (최대 100)", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "조회할 스터디 타입", example = "session")
            @RequestParam(required = false) String studyType
    ) {

        if (page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0 이상이어야 합니다.");
        }

        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("페이지 크기는 1에서 100 사이여야 합니다.");
        }

        return studyService.getAllStudies(page, size, studyType);
    }

    @GetMapping("/{id}")
    @Operation(summary = "스터디 개별 조회", description = "스터디 개별 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 스터디입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<StudyResponseDTO> getStudyById(@Parameter(description = "조회할 스터디 ID", example = "1") @PathVariable Long id) {
        Optional<StudyResponseDTO> studyDTO = studyService.getStudyById(id);
        return studyDTO.map(dto -> {
            // 기존 개행(\n)을 그대로 유지하여 변환
            dto.setStudyDescription(dto.getStudyDescription().replace("\r\n", "\n"));
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "스터디 수정", description = "스터디 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 스터디입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<StudyResponseDTO> updateStudy(
            @Parameter(description = "수정할 스터디 ID", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 스터디 수정 데이터", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyRequestDTO.class))
            ) @RequestBody @Valid StudyRequestDTO studyRequestDTO) {
        StudyResponseDTO updatedStudyResponseDTO = studyService.updateStudy(id, studyRequestDTO);
        return ResponseEntity.ok(updatedStudyResponseDTO);
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

    @PostMapping("/{id}/close")
    @Operation(summary = "스터디 마감", description = "지정된 스터디의 마감 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스터디 마감 상태가 성공적으로 변경되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 스터디입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "마감 권한이 없습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<StudyResponseDTO> closeStudy(@PathVariable Long id) {
        try {
            StudyResponseDTO updatedStudy = studyService.closeStudy(id);
            return ResponseEntity.ok(updatedStudy);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).build();
        }
    }
}
