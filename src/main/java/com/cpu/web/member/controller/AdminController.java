package com.cpu.web.member.controller;

import com.cpu.web.member.dto.response.MemberResponseDTO;
import com.cpu.web.member.service.AdminService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin", description = "관리자 API")
public class AdminController {

    private final AdminService adminService;

    // 전체 유저 조회
    @GetMapping("/user/all")
    @Operation(summary = "전체 유저 조회", description = "페이지네이션된 유저 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDTO.class)))
            }
    )
    public Page<MemberResponseDTO> getAllUsers(@Parameter(description = "페이지 번호 (0 이상)", example = "0")@RequestParam(defaultValue = "0") int page, @Parameter(description = "페이지 크기 (최대 100)", example = "10")@RequestParam(defaultValue = "10") int size) {
        if(page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0 이상이어야 합니다.");
        }

        if(size <= 0 || size >100) {
            throw new IllegalArgumentException("페이지 크기는 1에서 100 사이여야 합니다.");
        }
        return adminService.getAllUser(page, size);
    }

    // 특정 권한 가진 유저 전체 조회
    @GetMapping("/user/{role}")
    @Operation(summary = "특정 권한 유저 조회", description = "특정 권한 유저 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDTO.class)))
            }
    )
    public Page<MemberResponseDTO> getUsersByRole(@Parameter(description = "조회할 권한(admin or member or guest)", example = "admin") @PathVariable String role, @Parameter(description = "페이지 번호 (0 이상)", example = "0")@RequestParam(defaultValue = "0") int page, @Parameter(description = "페이지 크기 (최대 100)", example = "10")@RequestParam(defaultValue = "10") int size) {

        if(page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0 이상이어야 합니다.");
        }

        if(size <= 0 || size >100) {
            throw new IllegalArgumentException("페이지 크기는 1에서 100 사이여야 합니다.");
        }

        return adminService.getUsersByRole(role, page, size);
    }

    
    // 유저 권한 변경
    @PutMapping("/user/{id}")
    @Operation(summary = "유저 권한 변경", description = "유저 권한 변경 API")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponseDTO.class)))
    public ResponseEntity<MemberResponseDTO> updateRole(
            @Parameter(description = "수정할 유저의 ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "수정할 권한(admin or member or guest)", example = "admin")
            @RequestParam String role) {
        if (role.equals("admin") || role.equals("member") || role.equals("guest")) {
            MemberResponseDTO updateMemberDTO = adminService.updateRole(id, role);
            return ResponseEntity.ok(updateMemberDTO);
        }
        throw new IllegalArgumentException("유효하지 않은 권한을 부여하였습니다.");
    }

    // 유저 삭제
    @DeleteMapping("/user/{id}")
    @Operation(summary = "유저 삭제", description = "유저 삭제 API")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> deleteUser(@Parameter(description = "삭제할 유저의 ID", example = "1")@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 스터디 등록
    @PutMapping("/study/{id}")
    @Operation(summary = "스터디 등록", description = "스터디 등록 API")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> acceptStudy(@PathVariable Long id){
        StudyResponseDTO updateStudyResponseDTO =  adminService.acceptStudy(id);
        return ResponseEntity.ok(updateStudyResponseDTO);
    }

    // 스터디 등록 취소
    @PutMapping("/study/cancel/{id}")
    @Operation(summary = "스터디 등록 취소", description = "스터디 등록 API")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> unacceptStudy(@PathVariable Long id){
        StudyResponseDTO updateStudyRequestDTO =  adminService.unacceptStudy(id);
        return ResponseEntity.ok(updateStudyRequestDTO);
    }

    // 스터디 삭제
    @DeleteMapping("/study/{id}")
    @Operation(summary = "스터디 삭제", description = "스터디 삭제 API")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> deleteStudy(@PathVariable Long id) {
        adminService.deleteStudy(id);
        return ResponseEntity.noContent().build();
    }
}