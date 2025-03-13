package com.cpu.web.board.controller;

import com.cpu.web.board.dto.request.GatheringRequestDTO;
import com.cpu.web.board.dto.response.GatheringResponseDTO;
import com.cpu.web.board.entity.Gathering;
import com.cpu.web.board.service.GatheringService;
import com.cpu.web.scholarship.dto.response.StudyResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/gathering")
@Tag(name = "Gathering", description = "소모임 API")
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping
    @Operation(summary = "소모임 생성", description = "소모임 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyResponseDTO.class)))
    })
    public ResponseEntity<GatheringResponseDTO> createGathering(GatheringRequestDTO gatheringRequestDTO) {
        Gathering gathering = gatheringService.createGathering();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(gathering.getGatheringId())
                .toUri();
        return ResponseEntity.created(location).body(new GatheringResponseDTO());
    }

    @GetMapping
    @Operation(summary = "소모임 전체 조회", description = "페이지네이션된 소모임 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudyResponseDTO.class)))
    })
    public Page<GatheringResponseDTO> getAllGatherings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return gatheringService.getAllStudies(page, size);
    }
}
