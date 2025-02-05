package com.cpu.web.scholarship.controller;

import com.cpu.web.scholarship.service.ApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study/{studyId}/apply")
@Tag(name = "Study Apply", description = "스터디 신청 API")
public class ApplyController {

    private final ApplyService applyService;

    // ✅ 스터디 신청
    @PostMapping
    @Operation(summary = "스터디 신청", description = "스터디에 참여 신청을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "신청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<?> applyStudy(@PathVariable Long studyId) {
        applyService.applyStudy(studyId);
        return ResponseEntity.status(201).body("스터디 신청이 완료되었습니다.");
    }

    // ✅ 스터디 신청 취소
    @DeleteMapping
    @Operation(summary = "스터디 신청 취소", description = "스터디 신청을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    public ResponseEntity<?> cancelApply(@PathVariable Long studyId) {
        applyService.cancelApply(studyId);
        return ResponseEntity.noContent().build();
    }
}
