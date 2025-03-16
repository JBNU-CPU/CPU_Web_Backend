package com.cpu.web.board.controller;

import com.cpu.web.board.dto.request.GatheringRequestDTO;
import com.cpu.web.board.dto.request.PostRequestDTO;
import com.cpu.web.board.dto.response.GatheringResponseDTO;
import com.cpu.web.board.entity.Gathering;
import com.cpu.web.board.service.GatheringService;
import com.cpu.web.scholarship.dto.request.StudyRequestDTO;
import com.cpu.web.scholarship.dto.response.StudyResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/gathering")
@Tag(name = "Gathering", description = "소모임 API")
public class GatheringController {

    private final GatheringService gatheringService;

    // 소모임 생성
    @PostMapping
    @Operation(summary = "소모임 생성", description = "소모임 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GatheringResponseDTO.class)))
    })
    public ResponseEntity<GatheringResponseDTO> createGathering(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "소모임 개설 데이터",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GatheringRequestDTO.class))
            )  @RequestBody GatheringRequestDTO gatheringRequestDTO) {
        Gathering gathering = gatheringService.createGathering(gatheringRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(gathering.getGatheringId())
                .toUri();
        return ResponseEntity.created(location).body(new GatheringResponseDTO());
    }

    // 소모임 전체 조회
    @GetMapping
    @Operation(summary = "소모임 전체 조회", description = "페이지네이션된 소모임 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyResponseDTO.class)))
    })
    public Page<GatheringResponseDTO> getAllGatherings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return gatheringService.getAllStudies(page, size);
    }

    // 소모임 개별 조회
    @GetMapping("/{id}")
    @Operation(summary = "소모임 개별 조회", description = "소모임 개별 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 소모임입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<GatheringResponseDTO> getStudyById(@Parameter(description = "조회할 스터디 ID", example = "1") @PathVariable Long id) {
        Optional<GatheringResponseDTO> gatheringResponseDTO = gatheringService.getGatheringById(id);
        return gatheringResponseDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    @Operation(summary = "소모임 수정", description = "소모임 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 스터디입니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<GatheringResponseDTO> updateStudy(
            @Parameter(description = "수정할 스터디 ID", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 스터디 수정 데이터", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GatheringRequestDTO.class))
            ) @RequestBody GatheringRequestDTO gatheringRequestDTO) {
        GatheringResponseDTO updatedGatheringResponseDTO = gatheringService.updateGathering(id, gatheringRequestDTO);
        return ResponseEntity.ok(updatedGatheringResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "소모임 삭제", description = "소모임 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> deleteStudy(@PathVariable Long id) {
        gatheringService.deleteGathering(id);
        return ResponseEntity.noContent().build();
    }
}
