package com.cpu.web.board.controller;

import com.cpu.web.board.service.BoardApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gathering/apply")
@Tag(name = "소모임 신청", description = "소모임 신청 API")
public class BoardApplyController {
    private final BoardApplyService boardApplyService;
    //
    // 소모임 신청
    @PostMapping("/{gatheringId}")
    @Operation(summary = "소모임 신청", description = "소모임에 참여 신청을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "신청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<?> applyStudy(@PathVariable Long gatheringId) {
        boardApplyService.applyGathering(gatheringId);
        return ResponseEntity.status(201).body("소모임 신청이 완료되었습니다.");
    }

    // 소모임 신청 취소
    @DeleteMapping("/{gatheringId}")
    @Operation(summary = "소모임 신청 취소", description = "소모임 신청을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<?> cancelApply(@PathVariable Long gatheringId) {
        boardApplyService.cancelApply(gatheringId);
        return ResponseEntity.noContent().build();
    }
}
