package com.cpu.web.member.controller;

import com.cpu.web.member.dto.MemberDTO;
import com.cpu.web.member.service.AdminService;
import com.cpu.web.scholarship.dto.response.StudyResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    // 전체 유저 조회
    @GetMapping("/user")
    @Operation(summary = "전체 유저 조회", description = "전체 유저 조회 API")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public List<MemberDTO> getAllUsers() {
        return adminService.getAllUser();
    }

    // 유저 권한 변경
    @PutMapping("/user/{id}")
    @Operation(summary = "전체 유저 조회", description = "전체 유저 조회 API")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<MemberDTO> updateRole(@PathVariable Long id, @RequestParam String role) {
        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_MEMBER")) {
            MemberDTO updateMemberDTO = adminService.updateRole(id, role);
            return ResponseEntity.ok(updateMemberDTO);
        }
        throw new IllegalArgumentException("유효하지 않은 권한을 부여하였습니다.");
    }

    // 유저 삭제
    @DeleteMapping("/user/{id}")
    @Operation(summary = "유저 삭제", description = "유저 삭제 API")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 전체 스터디 조회
    @GetMapping("/study")
    @Operation(summary = "전체 스터디 조회", description = "전체 스터디 조회 API")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public List<StudyResponseDTO> getAllStudies() {return adminService.getAllStudy();}

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