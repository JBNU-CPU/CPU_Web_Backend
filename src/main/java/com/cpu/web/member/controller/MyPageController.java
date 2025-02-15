package com.cpu.web.member.controller;

import com.cpu.web.member.dto.CheckDTO;
import com.cpu.web.member.dto.MyPageEditDTO;
import com.cpu.web.member.dto.NewPasswordDTO;
import com.cpu.web.member.dto.response.MemberResponseDTO;
import com.cpu.web.member.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Tag(name = "Member", description = "회원 API")
public class MyPageController {

    private final MyPageService myPageService;
    
    // 내 정보 조회
    @GetMapping()
    @Operation(summary = "내 정보 조회", description = "내 정보 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저에 접근하였습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<MemberResponseDTO> getMyInformation(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<MemberResponseDTO> myInformationDTO = myPageService.getMyInformation(username);
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
            @Parameter(name = "password", description = "비밀번호", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format ="password", example = "qwer1234"))),
            @Parameter(name = "personName", description = "이름", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", example = "홍길동"))),
            @Parameter(name = "email", description = "이메일", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "email", example = "user@example.com")))
    })
    public ResponseEntity<MemberResponseDTO> updateMember(@RequestBody MyPageEditDTO myPageEditDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberResponseDTO updatedMemberDTO = myPageService.updateMember(myPageEditDTO, username);
        return ResponseEntity.ok(updatedMemberDTO);
    }

    // 아이디 및 이메일 검증
    @GetMapping("/check")
    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters({
            @Parameter(name = "username", description = "아이디", schema = @Schema(type = "String", minLength = 5, maxLength = 9, example = "202018556")),
            @Parameter(name = "email", description = "이메일")
    })
    public ResponseEntity<?> checkIdAndEmail(@RequestBody CheckDTO checkDTO){
        Boolean isChecked = myPageService.checkIdAndEmail(checkDTO);
        return ResponseEntity.ok(isChecked);
    }
    
    // 비밀번호 찾기
    @PostMapping("/password")
    @Operation(summary = "비밀번호 찾기", description = "비밀번호 재설정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters({
            @Parameter(name = "password", description = "비밀번호")
    })
    public ResponseEntity<?> setNewPassword(@RequestBody NewPasswordDTO newPasswordDTO){
        myPageService.setNewPassword(newPasswordDTO);
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
        myPageService.withdraw(username);
        return ResponseEntity.noContent().build();
    }
}
