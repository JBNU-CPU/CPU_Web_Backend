package com.cpu.web.member.controller;

import com.cpu.web.member.dto.request.CheckDTO;
import com.cpu.web.member.dto.request.MyPageEditDTO;
import com.cpu.web.member.dto.request.NewPasswordDTO;
import com.cpu.web.member.dto.response.MemberResponseDTO;
import com.cpu.web.member.dto.response.StudyOverviewDTO;
import com.cpu.web.member.service.MyInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Tag(name = "Member", description = "회원 API")
public class MyInformationController {

    private final MyInformationService myInformationService;
    
    // 내 정보 조회
    @GetMapping()
    @Operation(summary = "내 정보 조회", description = "내 정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저에 접근하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<MemberResponseDTO> getMyInformation(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<MemberResponseDTO> myInformationDTO = myInformationService.getMyInformation(username);
        return myInformationDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 회원 정보 수정
    @PutMapping()
    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters({
            @Parameter(name = "nickname", description = "닉네임", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "닉네임"))),
            @Parameter(name = "password", description = "비밀번호", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "password", example = "qwer1234"))),
            @Parameter(name = "personName", description = "이름", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "홍길동"))),
            @Parameter(name = "email", description = "이메일", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "email", example = "user@example.com"))),
            @Parameter(name = "phone", description = "전화번호", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "010-1234-5678")))
    })
    public ResponseEntity<MemberResponseDTO> updateMember(@RequestBody @Valid MyPageEditDTO myPageEditDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberResponseDTO updatedMemberDTO = myInformationService.updateMember(myPageEditDTO, username);
        return ResponseEntity.ok(updatedMemberDTO);
    }

    // 비밀번호 재설정
    @PostMapping("/password")
    @Operation(summary = "비밀번호 찾기", description = "비밀번호 재설정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters({
            @Parameter(name = "password", description = "비밀번호")
    })
    public ResponseEntity<?> setNewPassword(@RequestBody NewPasswordDTO newPasswordDTO){
        myInformationService.setNewPassword(newPasswordDTO);
        return ResponseEntity.ok().build();
    }
    
    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    @Operation(summary = "회원탈퇴", description = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<?> withdraw(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        myInformationService.withdraw(username);
        return ResponseEntity.noContent().build();
    }

    // 참여 스터디 목록
    @GetMapping("/joined-studies")
    @Operation(summary = "참여 중인 스터디 조회", description = "사용자의 스터디 참여 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<StudyOverviewDTO>> getMyJoinedStudies() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<StudyOverviewDTO> joinedStudies = myInformationService.getMyJoinedStudies(username);
        return ResponseEntity.ok(joinedStudies);
    }

    // 개설 스터디 목록
    @GetMapping("/opened-studies")
    @Operation(summary = "개설한 스터디 조회", description = "사용자의 스터디 개설 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<StudyOverviewDTO>> getMyLedStudies() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<StudyOverviewDTO> ledStudies = myInformationService.getMyLedStudies(username);
        return ResponseEntity.ok(ledStudies);
    }
}
